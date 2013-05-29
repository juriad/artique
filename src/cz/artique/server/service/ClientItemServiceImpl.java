package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientItemService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

public class ClientItemServiceImpl implements ClientItemService {

	public ListingResponse<UserItem> getItems(ListingRequest request) {
		if (request == null) {
			request = new ListingRequest();
		}
		ItemService is = new ItemService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return is.getItems(user, request);
	}

	public UserItem addManualItem(UserItem item) throws ValidationException {
		// FIXME call plan backup
		Validator<AddManualItem> validator = new Validator<AddManualItem>();
		validator.checkNullability(AddManualItem.USER_ITEM, false, item);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		item.setUser(user);
		item.setBackupBlobKey(null);
		item.setAdded(new Date());

		LabelService ls = new LabelService();
		List<Label> labelsByKeys = ls.getLabelsByKeys(item.getLabels());
		for (Label l : labelsByKeys) {
			validator.checkUser(AddManualItem.LABELS, user, l.getUser());
		}

		validator.checkNullability(AddManualItem.ITEM, false,
			item.getItemObject());
		if (!(item.getItemObject() instanceof ManualItem)) {
			throw new ValidationException(new Issue<AddManualItem>(
				AddManualItem.ITEM, IssueType.WRONG_TYPE));
		}
		ManualItem mi = (ManualItem) item.getItemObject();
		mi.setAdded(new Date());
		mi.setContent(validator.checkText(AddManualItem.ITEM_CONTENT,
			mi.getContent(), true, true));
		mi.setHash(null);
		mi.setPublished(null);
		mi.setTitle(validator.checkString(AddManualItem.ITEM_TITLE,
			mi.getTitle(), false, false));
		mi
			.setUrl(validator.checkUrl(AddManualItem.ITEM_URL, mi.getUrl(),
				false));
		mi.setContentType(ContentType.PLAIN_TEXT);

		ItemService is = new ItemService();
		is.addManualItem(item);
		return item;
	}

	public Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets) {
		// FIXME validate updateItems
		// FIXME call plan backup
		List<Key> itemKeys = new ArrayList<Key>();
		for (Key itemKey : changeSets.keySet()) {
			ChangeSet change = changeSets.get(itemKey);
			if (change.isEmpty()) {
				// empty
				continue;
			}
			if (!change.getUserItem().equals(itemKey)) {
				// invalid
				continue;
			}
			itemKeys.add(itemKey);
		}

		ItemService is = new ItemService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return is.updateItems(itemKeys, changeSets, user);
	}
}
