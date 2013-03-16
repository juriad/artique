package cz.artique.client.listFilters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.manager.Manager;

public interface ListFiltersManager<E> extends Manager {
	void addListFilter(E listFilter, AsyncCallback<E> ping);

	void deleteListFilter(E listFilter, AsyncCallback<Void> ping);

	void updateListFilter(E listFilter, AsyncCallback<E> ping);
}
