package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.ListFilter;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientListFilterService extends RemoteService {

	List<ListFilter> getAllListFilters();

	ListFilter addListFilter(ListFilter listFilter);
	
	ListFilter updateListFilter(ListFilter listFilter);
	
	void deleteListFilter(ListFilter listFilter);
}
