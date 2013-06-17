package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.client.items.ModifiedEvent;
import cz.artique.client.items.ModifiedHandler;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;

public class ArtiqueListProvider extends InfiniteListDataProvider
		implements ModifiedHandler {

	private HandlerRegistration addGeneralClickHandler;

	public ArtiqueListProvider(ListFilter listFilter, InfiniteList list) {
		super(listFilter, list);
		addGeneralClickHandler =
			Managers.ITEMS_MANAGER.addGeneralClickHandler(this);
	}

	@Override
	protected void applyFetchedData(ListingResponse<UserItem> result) {
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

	@Override
	protected void onStart() {
		Managers.waitForManagers(new ManagerReady() {
			public void onReady() {
				fetch(Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.LIST_INIT_SIZE)
					.get()
					.getI());
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
