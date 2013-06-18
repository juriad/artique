package cz.artique.client.service;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	public enum GetItems implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetItems";
		}
	}

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

	UserItem addManualItem(UserItem item) throws ValidationException;

	public enum UpdateItems implements HasIssue {
		LABELS,
		ITEMS,
		GENERAL;
		public String enumName() {
			return "UpdateItems";
		}
	}

	Map<Key, UserItem> updateItems(Map<Key, ChangeSet> changeSets)
			throws ValidationException;
}
