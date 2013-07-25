package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.service.ClientItemService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.BackupLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

/**
 * Provides methods which manipulate with {@link UserItem}s.
 * Methods are defined by communication interface.
 * 
 * @see ClientItemService
 * @author Adam Juraszek
 * 
 */
public class ClientItemServiceImpl implements ClientItemService {

	public ListingResponse getItems(ListingRequest request) {
		if (request == null) {
			request = new ListingRequest();
		}
		ItemService is = new ItemService();
		String userId = UserService.getCurrentUserId();
		return is.getItems(userId, request);
	}

	public UserItem addManualItem(UserItem item) throws ValidationException {
		Validator<AddManualItem> validator = new Validator<AddManualItem>();
		validator.checkNullability(AddManualItem.USER_ITEM, false, item);
		String userId = UserService.getCurrentUserId();
		item.setUserId(userId);
		item.setBackupBlobKey(null);
		item.setAdded(new Date());

		LabelService ls = new LabelService();
		List<Label> labelsByKeys = ls.getLabelsByKeys(item.getLabels());
		Label backupLabel = null;
		for (Label l : labelsByKeys) {
			validator.checkUser(AddManualItem.LABELS, userId, l.getUserId());
			if (l.getBackupLevel() != null
				|| !BackupLevel.NO_BACKUP.equals(l.getBackupLevel())) {
				backupLabel = l;
			}
		}

		validator.checkNullability(AddManualItem.ITEM, false,
			item.getItemObject());
		if (!(item.getItemObject() instanceof ManualItem)) {
			throw new ValidationException(new Issue<AddManualItem>(
				AddManualItem.ITEM, IssueType.INVALID_VALUE));
		}
		ManualItem mi = (ManualItem) item.getItemObject();
		mi.setAdded(new Date());
		mi.setContent(validator.checkText(AddManualItem.ITEM_CONTENT,
			mi.getContent(), true, true));
		mi.setHash(null);
		mi.setTitle(validator.checkString(AddManualItem.ITEM_TITLE,
			mi.getTitle(), false, false));
		mi
			.setUrl(validator.checkUrl(AddManualItem.ITEM_URL, mi.getUrl(),
				false));
		mi.setContentType(ContentType.PLAIN_TEXT);

		ItemService is = new ItemService();
		is.addManualItem(item);
		if (backupLabel != null) {
			BackupService bs = new BackupService();
			bs.doPlanForBackup(item, backupLabel);
		}
		return item;
	}

	public Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets)
			throws ValidationException {
		if (changeSets == null || changeSets.size() == 0) {
			return new HashMap<Key, UserItem>();
		}

		Validator<UpdateItems> validator = new Validator<UpdateItems>();
		String userId = UserService.getCurrentUserId();
		Set<Key> labelKeys = new HashSet<Key>();
		for (Key key : changeSets.keySet()) {
			ChangeSet change = changeSets.get(key);
			validator.checkNullability(UpdateItems.ITEMS, false, key);
			labelKeys.addAll(change.getLabelsAdded());
		}
		LabelService ls = new LabelService();
		List<Label> labels = ls.getLabelsByKeys(labelKeys);

		Map<Key, Label> backupLabels = new HashMap<Key, Label>();
		for (Label l : labels) {
			validator.checkUser(UpdateItems.LABELS, userId, l.getUserId());
			if (l.getBackupLevel() != null
				&& !BackupLevel.NO_BACKUP.equals(l.getBackupLevel())) {
				backupLabels.put(l.getKey(), l);
			}
		}

		ItemService is = new ItemService();
		List<UserItem> userItemsByKeys =
			is.getUserItemsByKeys(changeSets.keySet());

		BackupService bs = new BackupService();
		List<UserItem> toUpdate = new ArrayList<UserItem>();
		for (UserItem userItem : userItemsByKeys) {
			ChangeSet change = changeSets.get(userItem.getKey());
			if (change.isEmpty()) {
				// empty
				continue;
			}
			if (!change.getUserItem().equals(userItem.getKey())) {
				// invalid
				continue;
			}
			toUpdate.add(userItem);

			for (Key added : change.getLabelsAdded()) {
				Label backupLabel = backupLabels.get(added);
				if (backupLabel != null) {
					bs.doPlanForBackup(userItem, backupLabel);
					break;
				}
			}
		}

		return is.updateItems(userItemsByKeys, changeSets, userId);
	}
}
