package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.label.ListFilter;

public interface ClientListFilterServiceAsync {

	void getAllListFilters(AsyncCallback<List<ListFilter>> callback);

	void addListFilter(ListFilter listFilter, AsyncCallback<ListFilter> callback);

	void updateListFilter(ListFilter listFilter,
			AsyncCallback<ListFilter> callback);

	void deleteListFilter(ListFilter listFilter, AsyncCallback<Void> callback);

}
