package cz.artique.client.artique;

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

}
