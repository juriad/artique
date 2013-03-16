package cz.artique.client.artiqueListing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueItems.ModifiedEvent;
import cz.artique.client.artiqueItems.ModifiedHandler;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.manager.Managers;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueListProvider extends AbstractListDataProvider
		implements ModifiedHandler {

	private HandlerRegistration addGeneralClickHandler;

	public ArtiqueListProvider(ListFilter listFilter,
			InfiniteList<UserItem> list) {
		super(listFilter, list);
		addGeneralClickHandler =
			Managers.ITEMS_MANAGER.addGeneralClickHandler(this);
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
		return Managers.LABELS_MANAGER.isReady()
			&& Managers.CONFIG_MANAGER.isReady();
	}

	protected void onStart() {
		Managers.waitForManagers(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				fetch(Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.LIST_INIT_SIZE)
					.get()
					.getI());
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		}, Managers.LABELS_MANAGER, Managers.CONFIG_MANAGER);
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
