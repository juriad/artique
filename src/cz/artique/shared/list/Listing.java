package cz.artique.shared.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;

public class Listing {
	private final Filter filter;

	private List<UserItem> userItems;

	private Date lastFetch;

	private final ClientItemServiceAsync cis;

	private AsyncCallback<List<UserItem>> callback;

	public Listing(Filter filter, AsyncCallback<List<UserItem>> callback) {
		this.filter = filter;
		this.userItems = new ArrayList<UserItem>();
		this.lastFetch = new Date(0);
		this.callback = callback;

		this.cis = GWT.create(ClientItemService.class);
	}

	public void fetchUserItems(int count) {
		ListingUpdateRequest request =
			new ListingUpdateRequest(filter, lastFetch, userItems
				.get(0)
				.getKey(), userItems.get(userItems.size() - 1).getKey(), count);
		cis.getItems(request, new AsyncCallback<ListingUpdate<UserItem>>() {

			public void onSuccess(ListingUpdate<UserItem> result) {
				// TODO Auto-generated method stub

				callback.onSuccess(getUserItems());
			}

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	public Filter getFilter() {
		return filter;
	}

	public List<UserItem> getUserItems() {
		return userItems;
	}
}
