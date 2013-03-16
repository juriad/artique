package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientListFilterService;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.ListFilter;

public class ClientListFilterServiceImpl implements ClientListFilterService {

	public List<ListFilter> getAllListFilters() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ListFilterService lfs = new ListFilterService();
		return lfs.getAllListFilters(user);
	}

	public ListFilter addListFilter(ListFilter listFilter) {
		Sanitizer.checkUser("user", listFilter.getUser());
		Sanitizer.checkStringLength("name", listFilter.getName());
		Sanitizer.checkStringEmpty("name", listFilter.getName());
		if (listFilter.getFilterObject() != null) {
			Filter filter = listFilter.getFilterObject();
			Sanitizer.checkUser("filter-user", filter.getUser());
			Sanitizer.expectValue("filter-type", filter.getType(),
				FilterType.TOP_LEVEL_FILTER);
			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					Sanitizer.checkUser("filter-user", sub.getUser());
					Sanitizer.expectValue("filter-type", sub.getType(),
						FilterType.SECOND_LEVEL_FILTER);
				}
			}
		}
		ListFilterService lfs = new ListFilterService();
		return lfs.createListFilter(listFilter);
	}

	public ListFilter updateListFilter(ListFilter listFilter) {
		Sanitizer.checkUser("user", listFilter.getUser());
		Sanitizer.checkStringLength("name", listFilter.getName());
		Sanitizer.checkStringEmpty("name", listFilter.getName());
		if (listFilter.getFilterObject() != null) {
			Filter filter = listFilter.getFilterObject();
			Sanitizer.checkUser("filter-user", filter.getUser());
			Sanitizer.expectValue("filter-type", filter.getType(),
				FilterType.TOP_LEVEL_FILTER);
			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					Sanitizer.checkUser("filter-user", sub.getUser());
					Sanitizer.expectValue("filter-type", sub.getType(),
						FilterType.SECOND_LEVEL_FILTER);
				}
			}
		}
		ListFilterService lfs = new ListFilterService();
		lfs.updateListFilter(listFilter);
		return listFilter;
	}

	public void deleteListFilter(ListFilter listFilter) {
		Sanitizer.checkUser("user", listFilter.getUser());
		ListFilterService lfs = new ListFilterService();
		lfs.deleteListFilter(listFilter);
	}

}
