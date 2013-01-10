package cz.artique.client.artique2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import cz.artique.client.RpcWithTimeoutRequestBuilder;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.listing.InfiniteListDataProvider;
import cz.artique.client.listing.ListingSettings;
import cz.artique.client.listing.ScrollEndEvent;
import cz.artique.client.listing.ScrollEndEvent.ScrollEndType;
import cz.artique.client.listing.ScrollEndHandler;
import cz.artique.client.listing2.TestRowData;
import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.UserItem;

public class TestProvider implements InfiniteListDataProvider<TestRowData> {

	private Key first;

	private Key last;

	private Date lastFetch;

	private Date lastFetchProbe;

	private final ClientItemServiceAsync cis;

	private final ListingSettings settings;

	private Timer periodicFetch;

	private Integer wantCount;

	private int headSize;

	private boolean endReached;

	private final InfiniteList<TestRowData> list;

	public TestProvider(final ListingSettings settings,
			InfiniteList<TestRowData> list) {
		super();
		this.settings = settings;
		this.lastFetch = new Date(0);
		this.lastFetchProbe = new Date(0);
		this.cis = GWT.create(ClientItemService.class);
		((ServiceDefTarget) cis)
			.setRpcRequestBuilder(new RpcWithTimeoutRequestBuilder(settings
				.getTimeout()));

		this.list = list;
		list.clear();
		list.addScrollEndHandler(new ScrollEndHandler() {
			public void onScroll(ScrollEndEvent event) {
				if (ScrollEndType.BOTTOM.equals(event.getScrollEndType())) {
					// bottom end
					fetchUserItems(settings.getStep());
				}
			}
		});

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

		lastFetchProbe = new Date();
		ListingUpdateRequest request =
			new ListingUpdateRequest(settings.getFilter(), first, last, count,
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
		for (UserItem modified : result.getModified()) {
			// list.setValue(modified);
		}

		if (result.getHead().size() > 0) {
			first = result.getHead().get(0).getKey();
		}

		if (result.getTail().size() > 0) {
			last = result.getTail().get(result.getTail().size() - 1).getKey();
		}

		{
			List<TestRowData> l = new ArrayList<TestRowData>();
			for (UserItem ui : result.getTail()) {
				l.add(new TestRowData(ui.getItemObject().getTitle(), ui
					.getItemObject()
					.getContent()
					.getValue()));
			}
			list.appendValues(l);
		}

		// list.appendValues(result.getTail());

		{
			List<TestRowData> l = new ArrayList<TestRowData>();
			for (UserItem ui : result.getHead()) {
				l.add(new TestRowData(ui.getItemObject().getTitle(), ui
					.getItemObject()
					.getContent()
					.getValue()));
			}
			list.prependValues(l);
		}
		// list.prependValues(result.getHead());
		// list.setRowCount(-1, result.isEndReached());
		list.setRowCountExact(result.isEndReached());
		list.showTail();
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public int getHeadSize() {
		return headSize;
	}

	public void pushHead() {
		list.showHead();
		headSize = 0;
	}

	public boolean isEndReached() {
		return endReached;
	}

}
