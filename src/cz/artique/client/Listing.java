package cz.artique.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;

public class Listing extends AbstractDataProvider<UserItem> {
	private List<UserItem> userItems;

	private Date lastFetch;

	private Date lastFetchProbe;

	private final ClientItemServiceAsync cis;

	private final ListingSettings settings;

	private Timer periodicFetch;

	private Integer wantCount;

	private int headSize;

	private boolean endReached;

	public Listing(ListingSettings settings) {
		super();
		this.settings = settings;
		this.userItems = new ArrayList<UserItem>();
		this.lastFetch = new Date(0);
		this.lastFetchProbe = new Date(0);
		this.cis = GWT.create(ClientItemService.class);
		((ServiceDefTarget) cis)
			.setRpcRequestBuilder(new RpcWithTimeoutRequestBuilder(settings
				.getTimeout()));

		fetchUserItems(settings.getInitSize());

		periodicFetch = new Timer() {
			@Override
			public void run() {
				fetchUserItems(0);
			}
		};
		periodicFetch.scheduleRepeating(settings.getInterval());
	}

	protected void fetchUserItems(int count) {
		// check simultanous requests
		if (lastFetchProbe != null) {
			if (lastFetchProbe.getTime() + settings.getTimeout() > new Date()
				.getTime()) {
				// last request may still be running
				wantCount = count;
				return;
			}
		}

		if (wantCount != null && wantCount > count) {
			count = wantCount;
			wantCount = null;
		}

		// ok, we can make the request

		lastFetchProbe = new Date();
		ListingUpdateRequest request =
			new ListingUpdateRequest(settings.getFilter(), userItems
				.get(0)
				.getKey(), userItems.get(userItems.size() - 1).getKey(), count,
				settings.getRead(), lastFetch);
		cis.getItems(request, new AsyncCallback<ListingUpdate<UserItem>>() {

			public void onSuccess(ListingUpdate<UserItem> result) {
				endReached = result.isEndReached();
				applyFetchedData(result);
				lastFetch = result.getFetched();
			}

			public void onFailure(Throwable caught) {
				lastFetchProbe = null;
				// do nothing, will try later
			}
		});
	}

	protected void applyFetchedData(ListingUpdate<UserItem> result) {
		int minModified = Integer.MAX_VALUE;
		int maxModified = Integer.MIN_VALUE;
		// modified
		{
			int j = 0;
			for (int i = 0; i < result.getModified().size(); i++) {
				UserItem modified = result.getModified().get(i);
				for (; j < userItems.size(); j++) {
					minModified = Math.min(j, minModified);
					maxModified = Math.max(j + 1, maxModified);
					UserItem local = userItems.get(j);
					if (local.equals(modified)) {
						modified.setItem(local.getItem());
						userItems.set(j, modified);
					}
				}
			}
		}

		// tail
		{
			if (result.getTail().size() > 0) {
				minModified = Math.min(userItems.size(), minModified);
				userItems.addAll(result.getTail());
				maxModified = userItems.size();
			}
		}

		if (minModified < Integer.MAX_VALUE) {
			updateRowData(minModified,
				userItems.subList(minModified, maxModified));
			updateRowCount(userItems.size()-getHeadSize(), isEndReached());
		}

		// head
		{
			userItems.addAll(0, result.getHead());
			headSize += result.getHead().size();
		}
	}

	public int getHeadSize() {
		return headSize;
	}

	public void pushHead() {
		updateRowData(0, userItems);
		headSize = 0;
	}

	public List<UserItem> getUserItems() {
		return userItems;
	}

	@Override
	protected void onRangeChanged(HasData<UserItem> display) {
		int size = userItems.size();
		if (size > 0) {
			// Do not push data if the data set is empty.
			updateRowData(display, 0,
				userItems.subList(getHeadSize(), userItems.size()));
			updateRowCount(userItems.size()-getHeadSize(), isEndReached());
		}
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public boolean isEndReached() {
		return endReached;
	}
}
