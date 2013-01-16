package cz.artique.client.items;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.Manager;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;

public interface ItemsManager<E, F> extends Manager {
	void labelAdded(E userItem, F label, AsyncCallback<Void> ping);

	void labelRemoved(E userItem, F label, AsyncCallback<Void> ping);

	void readSet(E userItem, boolean read, AsyncCallback<Void> ping);

	void getItems(ListingUpdateRequest request,
			AsyncCallback<ListingUpdate<E>> ping);
}
