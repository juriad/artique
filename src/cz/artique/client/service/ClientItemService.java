package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

/**
 * The service responsible for {@link UserItem}s passing and related operations
 * between client and server.
 * 
 * @author Adam Juraszek
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	public enum GetItems implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetItems";
		}
	}

	/**
	 * Gets list of {@link UserItem}s matching {@link ListFilter} restricted by
	 * range of keys.
	 * 
	 * @param request
	 *            request describing desired {@link UserItem}s
	 * @return response containing list of matched {@link UserItem}
	 */
	ListingResponse getItems(ListingRequest request);

	public enum AddManualItem implements HasIssue {
		USER_ITEM,
		ITEM,
		LABELS,
		ITEM_CONTENT,
		ITEM_TITLE,
		ITEM_URL,
		GENERAL;
		public String enumName() {
			return "AddManualItem";
		}
	}

	/**
	 * Creates a new {@link ManualItem} and its {@link UserItem} for the
	 * {@link ManualSource} of current user.
	 * 
	 * @param item
	 *            UserItem to be added to {@link ManualSource} as
	 *            {@link ManualItem}
	 * @return added {@link UserItem} of {@link ManualItem}
	 * @throws ValidationException
	 *             if validation of the {@link UserItem} fails
	 */
	UserItem addManualItem(UserItem item) throws ValidationException;

	public enum UpdateItems implements HasIssue {
		LABELS,
		ITEMS,
		GENERAL;
		public String enumName() {
			return "UpdateItems";
		}
	}

	/**
	 * Updates {@link UserItem}s by set of changes (labels added, removed and
	 * new read
	 * state).
	 * 
	 * @param changeSets
	 *            set of changes for each {@link UserItem} changed
	 * @return new values of changed {@link UserItem}s
	 * @throws ValidationException
	 *             if validation of the changes fails
	 */
	Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets)
			throws ValidationException;
}
