package cz.artique.client.artiqueListing;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueItems.ArtiqueItemsManager;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.listing.InfiniteListDataProvider;
import cz.artique.client.listing.ListingSettings;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.FilterOrder;

public class AbstractListDataProvider
		implements InfiniteListDataProvider<UserItem> {
	private Key first;

	private Key last;

	private Date lastFetch;

	private Date lastFetchProbeDate;

	private int lastFetchProbeCount;

	private final ListingSettings settings;

	private final Timer periodicTimer;

	private Integer wantCount;

	private boolean endReached;

	private final InfiniteList<UserItem> list;

	private final ArtiqueItemsManager manager;

	private final FilterOrder order;

	public AbstractListDataProvider(final ListingSettings settings,
			InfiniteList<UserItem> list) {
		super();
		this.settings = settings;
		this.lastFetch = new Date(0);
		this.lastFetchProbeDate = new Date(0);
		this.manager = ArtiqueItemsManager.MANAGER;
		if (settings.getListFilter() != null) {
			order = settings.getListFilter().getOrder();
		} else {
			order = FilterOrder.DESCENDING;
		}

		this.list = list;
		list.setProvider(this);

		periodicTimer = new Timer() {
			@Override
			public void run() {
				fetch(0);
			}
		};
		periodicTimer.scheduleRepeating(settings.getInterval());
		fetch(settings.getInitSize());
	}

	public boolean fetch(int count) {
		GWT.log("fetching: " + count);
		if (count < 0) {
			count = settings.getStep();
		}
		// check simultanous requests
		if (lastFetchProbeDate != null) {
			if (lastFetchProbeDate.getTime() + manager.getTimeout() > new Date()
				.getTime()) {
				// last request may still be running
				if (count > lastFetchProbeCount) {
					count -= lastFetchProbeCount;
					if (wantCount != null) {
						if (wantCount < count) {
							wantCount = count;
						}
					} else {
						wantCount = count;
					}
				}
				return false;
			}
		}

		if (wantCount != null && wantCount > count) {
			count = wantCount;
			wantCount = null;
		}

		if (endReached && FilterOrder.DESCENDING.equals(order)) {
			wantCount = 0;
		}

		GWT.log("do fetch " + count);

		// ok, we can make the request
		doFetch(count);
		return true;
	}

	protected void doFetch(int count) {
		lastFetchProbeDate = new Date();
		lastFetchProbeCount = count;
		ListingUpdateRequest request =
			new ListingUpdateRequest(settings.getListFilter(), first, last,
				lastFetch, count);
		manager.getItems(request, new AsyncCallback<ListingUpdate<UserItem>>() {

			public void onSuccess(ListingUpdate<UserItem> result) {
				lastFetchProbeDate = null;
				if (!endReached) {
					endReached = result.isEndReached();
				}
				applyFetchedData(result);
				lastFetch = result.getFetched();
			}

			public void onFailure(Throwable caught) {
				lastFetchProbeDate = null;
				// do nothing, will try later
			}
		});
	}

	protected void applyFetchedData(ListingUpdate<UserItem> result) {
		for (UserItem modified : result.getModified()) {
			getList().setValue(modified);
		}

		if (FilterOrder.ASCENDING.equals(order)) {
			// no head
			if (!result.getTail().isEmpty()) {
				if (first == null) {
					first = result.getTail().get(0).getKey();
				}
				last =
					result.getTail().get(result.getTail().size() - 1).getKey();
			}
		} else {
			if (!result.getHead().isEmpty()) {
				last = result.getHead().get(0).getKey();
			}
			if (!result.getTail().isEmpty()) {
				if (last == null) {
					last = result.getTail().get(0).getKey();
				}
				first =
					result.getTail().get(result.getTail().size() - 1).getKey();
			}
		}

		getList().appendValues(result.getTail());
		getList().prependValues(result.getHead());
		getList().setRowCountExact(isEndReached());
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public boolean isEndReached() {
		return endReached;
	}

	public InfiniteList<UserItem> getList() {
		return list;
	}
}