package cz.artique.client.artique;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import cz.artique.client.RpcWithTimeoutRequestBuilder;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.listing.InfiniteListDataProvider;
import cz.artique.client.listing.ListingSettings;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;

public class AbstractListDataProvider
		implements InfiniteListDataProvider<UserItem> {
	private Key first;

	private Key last;

	private Date lastFetch;

	private Date lastFetchProbe;

	private final ClientItemServiceAsync cis;

	private final ListingSettings settings;

	private Timer periodicTimer;

	private Integer wantCount;

	private boolean endReached;

	private final InfiniteList<UserItem> list;

	public AbstractListDataProvider(final ListingSettings settings,
			InfiniteList<UserItem> list) {
		super();
		this.settings = settings;
		this.lastFetch = new Date(0);
		this.lastFetchProbe = new Date(0);
		this.cis = GWT.create(ClientItemService.class);
		((ServiceDefTarget) cis)
			.setRpcRequestBuilder(new RpcWithTimeoutRequestBuilder(settings
				.getTimeout()));

		this.list = list;
		list.setProvider(this);

		periodicTimer = new Timer() {
			@Override
			public void run() {
				fetch(0);
			}
		};
		periodicTimer.scheduleRepeating(settings.getInterval());
	}

	public void fetch(int count) {
		if (count < 0) {
			count = settings.getStep();
		}
		// check simultanous requests
		if (lastFetchProbe != null) {
			if (lastFetchProbe.getTime() + settings.getTimeout() > new Date()
				.getTime()) {
				// last request may still be running
				if (wantCount != null) {
					if (wantCount < count) {
						wantCount = count;
					}
				} else {
					wantCount = count;
				}
				return;
			}
		}

		if (wantCount != null && wantCount > count) {
			count = wantCount;
			wantCount = null;
		}

		if (endReached) {
			wantCount = 0;
		}

		// ok, we can make the request
		doFetch(count);

	}

	protected void doFetch(int count) {
		lastFetchProbe = new Date();
		ListingUpdateRequest request =
			new ListingUpdateRequest(settings.getFilter(), first, last, count,
				settings.getRead(), lastFetch);
		cis.getItems(request, new AsyncCallback<ListingUpdate<UserItem>>() {

			public void onSuccess(ListingUpdate<UserItem> result) {
				endReached = result.isEndReached();
				applyFetchedData(result);
				lastFetch = result.getFetched();
				if (endReached) {
					periodicTimer.cancel();
				}
			}

			public void onFailure(Throwable caught) {
				lastFetchProbe = null;
				// do nothing, will try later
			}
		});
	}

	protected void applyFetchedData(ListingUpdate<UserItem> result) {
		for (UserItem modified : result.getModified()) {
			list.setValue(modified);
		}

		if (result.getHead().size() > 0) {
			first = result.getHead().get(0).getKey();
		}

		if (result.getTail().size() > 0) {
			last = result.getTail().get(result.getTail().size() - 1).getKey();
		}

		list.appendValues(result.getTail());
		list.prependValues(result.getHead());
		list.setRowCountExact(isEndReached());
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public boolean isEndReached() {
		return endReached;
	}
}
