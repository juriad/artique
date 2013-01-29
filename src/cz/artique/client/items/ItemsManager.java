package cz.artique.client.items;


import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.manager.Manager;
import cz.artique.shared.items.ListingUpdate;
import cz.artique.shared.items.ListingUpdateRequest;

public interface ItemsManager<E, F> extends Manager {
	void labelAdded(E userItem, F label, AsyncCallback<E> ping);

	void labelRemoved(E userItem, F label, AsyncCallback<E> ping);

	void readSet(E userItem, boolean read, AsyncCallback<E> ping);

	void getItems(ListingUpdateRequest request,
			AsyncCallback<ListingUpdate<E>> ping);
}
