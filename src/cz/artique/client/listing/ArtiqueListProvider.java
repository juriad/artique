package cz.artique.client.listing;

import com.google.gwt.event.shared.HandlerRegistration;

import cz.artique.client.config.ConfigManager;
import cz.artique.client.items.ModifiedEvent;
import cz.artique.client.items.ModifiedHandler;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;

/**
 * Concrete implementation of {@link InfiniteListDataProvider} for
 * {@link ArtiqueList}.
 * It is aware of managers.
 * 
 * @author Adam Juraszek
 * 
 */
public class ArtiqueListProvider extends InfiniteListDataProvider
		implements ModifiedHandler {

	private HandlerRegistration addGeneralClickHandler;

	public ArtiqueListProvider(ListFilter listFilter, InfiniteList list) {
		super(listFilter, list);
		addGeneralClickHandler =
			Managers.ITEMS_MANAGER.addModifiedHandler(this);

		Managers.waitForManagers(new ManagerReady() {
			public void onReady() {
				fetch(Managers.CONFIG_MANAGER
					.getConfig(ClientConfigKey.LIST_INIT_SIZE)
					.get()
					.getI());
			}
		}, Managers.LABELS_MANAGER, Managers.CONFIG_MANAGER);
	}

	/**
	 * Automatically show tail fetched data.
	 * 
	 * @see cz.artique.client.listing.InfiniteListDataProvider#applyFetchedData(cz.artique.shared.items.ListingResponse)
	 */
	@Override
	protected void applyFetchedData(ListingResponse result) {
		super.applyFetchedData(result);
		if (canceled) {
			return;
		}
		getList().showTail();
	}

	/**
	 * Ready when are ready {@link LabelsManager} and {@link ConfigManager}s.
	 * 
	 * @see cz.artique.client.listing.InfiniteListDataProvider#isReady()
	 */
	@Override
	protected boolean isReady() {
		return Managers.LABELS_MANAGER.isReady()
			&& Managers.CONFIG_MANAGER.isReady();
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
