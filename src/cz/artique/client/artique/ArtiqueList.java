package cz.artique.client.artique;

import cz.artique.client.listing.AbstractInfiniteList;
import cz.artique.client.listing.InfiniteListCell;
import cz.artique.client.listing.InfiniteListInfo;
import cz.artique.shared.model.item.UserItem;

public class ArtiqueList extends AbstractInfiniteList<UserItem> {

	// TODO 
	public ArtiqueList(InfiniteListCell<UserItem> cell, InfiniteListInfo info) {
		super(cell, info);
	}

}
