package cz.artique.client.artiqueListing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueItems.ModifiedEvent;
import cz.artique.client.artiqueItems.ModifiedHandler;
import cz.artique.client.artiqueLabels.ArtiqueLabelsManager;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.listing.ListingSettings;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.model.item.UserItem;

public class ArtiqueListProvider extends AbstractListDataProvider
		implements ModifiedHandler {

	private HandlerRegistration addGeneralClickHandler;

	public ArtiqueListProvider(ListingSettings settings,
			InfiniteList<UserItem> list) {
		super(settings, list);
		addGeneralClickHandler = manager.addGeneralClickHandler(this);
	}

	@Override
	protected void applyFetchedData(ListingUpdate<UserItem> result) {
		super.applyFetchedData(result);
		if (canceled) {
			return;
		}
		getList().showTail();
	}

	@Override
	protected boolean isReady() {
		return ArtiqueLabelsManager.MANAGER.isReady();
	}

	protected void onStart() {
		ArtiqueLabelsManager.MANAGER.ready(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				fetch(getSettings().getInitSize());
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		addGeneralClickHandler.removeHandler();
		addGeneralClickHandler = null;
	}

	public void onModified(ModifiedEvent e) {
		if (canceled) {
			return;
		}
		for (UserItem ui : e.getModified().values()) {
			getList().setValue(ui);
		}
	}
}
