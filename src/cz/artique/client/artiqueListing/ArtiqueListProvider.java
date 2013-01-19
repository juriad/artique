package cz.artique.client.artiqueListing;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueLabels.ArtiqueLabelsManager;
import cz.artique.client.listing.InfiniteList;
import cz.artique.client.listing.ListingSettings;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.model.item.UserItem;

public class ArtiqueListProvider extends AbstractListDataProvider {

	public ArtiqueListProvider(ListingSettings settings,
			InfiniteList<UserItem> list) {
		super(settings, list);
	}

	@Override
	protected void applyFetchedData(ListingUpdate<UserItem> result) {
		super.applyFetchedData(result);
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

}
