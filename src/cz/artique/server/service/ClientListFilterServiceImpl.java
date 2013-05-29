package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientListFilterService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.validation.ValidationException;

public class ClientListFilterServiceImpl implements ClientListFilterService {

	public List<ListFilter> getAllListFilters() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ListFilterService lfs = new ListFilterService();
		return lfs.getAllListFilters(user);
	}

	public ListFilter addListFilter(ListFilter listFilter)
			throws ValidationException {
		Validator<AddListFilter> validator = new Validator<AddListFilter>();
		validator
			.checkNullability(AddListFilter.LIST_FILTER, false, listFilter);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		listFilter.setUser(user);
		listFilter.setName(validator.checkString(AddListFilter.NAME,
			listFilter.getName(), false, false));
		listFilter.setHierarchy(validator.checkString(AddListFilter.HIERARCHY,
			listFilter.getHierarchy(), false, false));
		listFilter.setExportAlias(validator.checkString(
			AddListFilter.EXPORT_ALIAS, listFilter.getExportAlias(), true,
			false));

		if (listFilter.getFilterObject() != null) {
			Filter filter = listFilter.getFilterObject();
			filter.setUser(user);
			filter.setType(FilterType.TOP_LEVEL_FILTER);
			LabelService ls = new LabelService();
			if (filter.getLabels() != null) {
				List<Label> labelsByKeys =
					ls.getLabelsByKeys(filter.getLabels());
				for (Label l : labelsByKeys) {
					validator.checkUser(AddListFilter.FILTER_LABELS, user,
						l.getUser());
				}
			}

			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					sub.setUser(user);
					sub.setType(FilterType.SECOND_LEVEL_FILTER);
					if (sub.getLabels() != null) {
						List<Label> labelsByKeys =
							ls.getLabelsByKeys(filter.getLabels());
						for (Label l : labelsByKeys) {
							validator.checkUser(AddListFilter.FILTER_LABELS,
								user, l.getUser());
						}
					}
				}
			}
		}
		ListFilterService lfs = new ListFilterService();
		lfs.createListFilter(listFilter);
		return listFilter;
	}

	public ListFilter updateListFilter(ListFilter listFilter)
			throws ValidationException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		Validator<UpdateListFilter> validator =
			new Validator<UpdateListFilter>();
		validator.checkNullability(UpdateListFilter.LIST_FILTER, false,
			listFilter);
		validator.checkNullability(UpdateListFilter.LIST_FILTER, false,
			listFilter.getKey());

		ListFilterService lfs = new ListFilterService();
		ListFilter listFilterByKey =
			lfs.getListFilterByKey(listFilter.getKey());
		validator.checkUser(UpdateListFilter.LIST_FILTER, user,
			listFilterByKey.getUser());

		listFilter.setUser(user);
		listFilter.setName(validator.checkString(UpdateListFilter.NAME,
			listFilter.getName(), false, false));
		listFilter
			.setHierarchy(validator.checkString(UpdateListFilter.HIERARCHY,
				listFilter.getHierarchy(), false, false));
		listFilter.setExportAlias(validator.checkString(
			UpdateListFilter.EXPORT_ALIAS, listFilter.getExportAlias(), true,
			false));

		if (listFilter.getFilterObject() != null) {
			Filter filter = listFilter.getFilterObject();
			filter.setUser(user);
			filter.setType(FilterType.TOP_LEVEL_FILTER);
			LabelService ls = new LabelService();
			if (filter.getLabels() != null) {
				List<Label> labelsByKeys =
					ls.getLabelsByKeys(filter.getLabels());
				for (Label l : labelsByKeys) {
					validator.checkUser(UpdateListFilter.FILTER_LABELS, user,
						l.getUser());
				}
			}

			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					sub.setUser(user);
					sub.setType(FilterType.SECOND_LEVEL_FILTER);
					if (sub.getLabels() != null) {
						List<Label> labelsByKeys =
							ls.getLabelsByKeys(filter.getLabels());
						for (Label l : labelsByKeys) {
							validator.checkUser(UpdateListFilter.FILTER_LABELS,
								user, l.getUser());
						}
					}
				}
			}
		}

		lfs.updateListFilter(listFilter);
		return listFilter;
	}

	public void deleteListFilter(ListFilter listFilter)
			throws ValidationException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		Validator<DeleteListFilter> validator =
			new Validator<DeleteListFilter>();
		validator.checkNullability(DeleteListFilter.LIST_FILTER, false,
			listFilter);
		validator.checkNullability(DeleteListFilter.LIST_FILTER, false,
			listFilter.getKey());

		ListFilterService lfs = new ListFilterService();
		ListFilter listFilterByKey =
			lfs.getListFilterByKey(listFilter.getKey());
		validator.checkUser(DeleteListFilter.LIST_FILTER, user,
			listFilterByKey.getUser());

		lfs.deleteListFilter(listFilter);
	}

}
