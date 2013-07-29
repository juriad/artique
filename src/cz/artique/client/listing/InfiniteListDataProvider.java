package cz.artique.client.listing;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.items.ItemsManager;
import cz.artique.client.manager.Managers;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;

/**
 * Provider of data for {@link InfiniteList}; the provider communicates with
 * {@link ItemsManager}.
 * 
 * It keeps track of first and last known {@link UserItem} and current
 * {@link ListFilter}.
 * 
 * @author Adam Juraszek
 * 
 */
public class InfiniteListDataProvider {
	private Key first;

	private Key last;

	private Date lastFetch;

	private Date lastFetchProbeDate;

	private int lastFetchProbeCount;

	private final Timer periodicTimer;

	private Integer wantCount;

	private boolean endReached;

	private final InfiniteList list;

	private final ListFilterOrder order;

	protected boolean canceled = false;

	private final ListFilter listFilter;

	/**
	 * Constructs new provider for {@link InfiniteList} providing
	 * {@link UserItem}s matched by {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be matched by {@link UserItem}s
	 * @param list
	 *            provides data for this {@link InfiniteList}
	 */
	public InfiniteListDataProvider(ListFilter listFilter, InfiniteList list) {
		super();
		this.listFilter = listFilter;
		this.lastFetch = new Date(0);
		this.lastFetchProbeDate = new Date(0);
		if (listFilter != null) {
			order = listFilter.getOrder();
		} else {
			order = ListFilterOrder.getDefault();
		}

		this.list = list;
		list.setProvider(this);

		periodicTimer = new Timer() {
			@Override
			public void run() {
				fetch(0);
			}
		};
		periodicTimer.scheduleRepeating(Managers.CONFIG_MANAGER
			.getConfig(ClientConfigKey.LIST_FETCH_INTERVAL)
			.get()
			.getI());
	}

	/**
	 * @return whether provider is ready to provide {@link UserItem}s
	 */
	protected boolean isReady() {
		return true;
	}

	/**
	 * Checks preconditions before actual fetch and calls {@link #doFetch(int)}.
	 * 
	 * @param count
	 *            maximum number of {@link UserItem}s to fetch
	 * @return whether the fetch will be performed
	 */
	public boolean fetch(int count) {
		if (canceled) {
			return false;
		}
		if (count < 0) {
			count =
				Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.LIST_FETCH_STEP)
					.get()
					.getI();
		}
		// check simultanous requests
		if (lastFetchProbeDate != null) {
			if (lastFetchProbeDate.getTime()
				+ Managers.ITEMS_MANAGER.getTimeout() > new Date().getTime()
				|| !isReady()) {
				// last request may still be running or not ready yet
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
			} else {
				lastFetchProbeDate = null;
			}
		}

		if (wantCount != null && wantCount > count) {
			count = wantCount;
			wantCount = null;
		}

		if (endReached && ListFilterOrder.DESCENDING.equals(order)) {
			wantCount = 0;
		}

		// ok, we can make the request
		doFetch(count);
		return true;
	}

	/**
	 * Does the actual fetching.
	 * 
	 * @param count
	 *            number of {@link UserItem} to be requested
	 */
	protected void doFetch(int count) {
		if (lastFetchProbeDate != null) {
			return;
		}
		lastFetchProbeDate = new Date();
		lastFetchProbeCount = count;
		ListingRequest request =
			new ListingRequest(listFilter, first, last, count);
		Managers.ITEMS_MANAGER.getItems(request,
			new AsyncCallback<ListingResponse>() {

				public void onSuccess(ListingResponse result) {
					lastFetch = result.getFetched();
					if (!endReached) {
						endReached = result.isEndReached();
					}
					lastFetchProbeDate = null;

					applyFetchedData(result);
				}

				public void onFailure(Throwable caught) {
					lastFetchProbeDate = null;
				}
			});
	}

	/**
	 * Applies fetched data to the {@link InfiniteList}.
	 * 
	 * @param result
	 *            result of fetch
	 */
	protected void applyFetchedData(ListingResponse result) {
		if (canceled) {
			return;
		}

		if (ListFilterOrder.ASCENDING.equals(order)) {
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
		getList().setEndReached(result.isEndReached());
	}

	/**
	 * @return date of last fetch
	 */
	public Date getLastFetch() {
		return lastFetch;
	}

	/**
	 * @return whether there is no more data
	 */
	public boolean isEndReached() {
		return endReached;
	}

	/**
	 * @return {@link InfiniteList} this provider is providing data for
	 */
	public InfiniteList getList() {
		return list;
	}

	/**
	 * Destroys this provider.
	 */
	public void destroy() {
		this.canceled = true;
		periodicTimer.cancel();
	}
}
