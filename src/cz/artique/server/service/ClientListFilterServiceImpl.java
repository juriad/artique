package cz.artique.server.service;

import java.util.List;

import cz.artique.client.service.ClientListFilterService;
import cz.artique.client.service.ClientSourceService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

/**
 * Provides methods which manipulate with {@link ListFilter}s.
 * Methods are defined by communication interface.
 * 
 * @see ClientSourceService
 * @author Adam Juraszek
 * 
 */
public class ClientListFilterServiceImpl implements ClientListFilterService {

	public List<ListFilter> getAllListFilters() {
		String userId = UserService.getCurrentUserId();
		ListFilterService lfs = new ListFilterService();
		return lfs.getAllListFilters(userId);
	}

	public ListFilter addListFilter(ListFilter listFilter)
			throws ValidationException {
		Validator<AddListFilter> validator = new Validator<AddListFilter>();
		validator
			.checkNullability(AddListFilter.LIST_FILTER, false, listFilter);
		String userId = UserService.getCurrentUserId();
		listFilter.setUserId(userId);
		listFilter.setName(validator.checkString(AddListFilter.NAME,
			listFilter.getName(), false, false));
		listFilter.setHierarchy(validator.checkString(AddListFilter.HIERARCHY,
			listFilter.getHierarchy(), false, false));
		listFilter.setExportAlias(validator.checkString(
			AddListFilter.EXPORT_ALIAS, listFilter.getExportAlias(), true,
			false));
		if (listFilter.getExportAlias() != null) {
			String value = listFilter.getExportAlias();
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (Character.isWhitespace(c)) {
					throw new ValidationException(new Issue<AddListFilter>(
						AddListFilter.EXPORT_ALIAS, IssueType.INVALID_VALUE));
				}
			}
		}

		if (listFilter.getFilterObject() != null) {
			Filter filter = listFilter.getFilterObject();
			filter.setUserId(userId);
			filter.setLevel(FilterLevel.TOP_LEVEL_FILTER);
			LabelService ls = new LabelService();
			if (filter.getLabels() != null) {
				List<Label> labelsByKeys =
					ls.getLabelsByKeys(filter.getLabels());
				for (Label l : labelsByKeys) {
					validator.checkUser(AddListFilter.FILTER_LABELS, userId,
						l.getUserId());
				}
			}

			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					sub.setUserId(userId);
					sub.setLevel(FilterLevel.SECOND_LEVEL_FILTER);
					if (sub.getLabels() != null) {
						List<Label> labelsByKeys =
							ls.getLabelsByKeys(filter.getLabels());
						for (Label l : labelsByKeys) {
							validator.checkUser(AddListFilter.FILTER_LABELS,
								userId, l.getUserId());
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
		String userId = UserService.getCurrentUserId();
		Validator<UpdateListFilter> validator =
			new Validator<UpdateListFilter>();
		validator.checkNullability(UpdateListFilter.LIST_FILTER, false,
			listFilter);
		validator.checkNullability(UpdateListFilter.LIST_FILTER, false,
			listFilter.getKey());

		ListFilterService lfs = new ListFilterService();
		ListFilter listFilterByKey =
			lfs.getListFilterByKey(listFilter.getKey());
		validator.checkUser(UpdateListFilter.LIST_FILTER, userId,
			listFilterByKey.getUserId());

		listFilter.setUserId(userId);
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
			filter.setUserId(userId);
			filter.setLevel(FilterLevel.TOP_LEVEL_FILTER);
			LabelService ls = new LabelService();
			if (filter.getLabels() != null) {
				List<Label> labelsByKeys =
					ls.getLabelsByKeys(filter.getLabels());
				for (Label l : labelsByKeys) {
					validator.checkUser(UpdateListFilter.FILTER_LABELS, userId,
						l.getUserId());
				}
			}

			if (filter.getFilterObjects() != null) {
				for (Filter sub : filter.getFilterObjects()) {
					sub.setUserId(userId);
					sub.setLevel(FilterLevel.SECOND_LEVEL_FILTER);
					if (sub.getLabels() != null) {
						List<Label> labelsByKeys =
							ls.getLabelsByKeys(filter.getLabels());
						for (Label l : labelsByKeys) {
							validator.checkUser(UpdateListFilter.FILTER_LABELS,
								userId, l.getUserId());
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
		String userId = UserService.getCurrentUserId();
		Validator<DeleteListFilter> validator =
			new Validator<DeleteListFilter>();
		validator.checkNullability(DeleteListFilter.LIST_FILTER, false,
			listFilter);
		validator.checkNullability(DeleteListFilter.LIST_FILTER, false,
			listFilter.getKey());

		ListFilterService lfs = new ListFilterService();
		ListFilter listFilterByKey =
			lfs.getListFilterByKey(listFilter.getKey());
		validator.checkUser(DeleteListFilter.LIST_FILTER, userId,
			listFilterByKey.getUserId());

		lfs.deleteListFilter(listFilter);
	}

}
