package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientLabelService;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyTooLongException;
import cz.artique.shared.utils.PropertyValueException;
import cz.artique.shared.utils.SecurityBreachException;

public class ClientLabelServiceImpl implements ClientLabelService {

	public List<Label> getAllLabels() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		LabelService ls = new LabelService();
		return ls.getAllLabels(user);
	}

	public Label addLabel(Label label)
			throws NullPointerException, PropertyTooLongException,
			PropertyEmptyException, SecurityBreachException {
		if (label == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUser("user", label.getUser());
		Sanitizer.checkStringEmpty("name", label.getName());
		Sanitizer.checkStringLength("name", label.getName());

		LabelService ls = new LabelService();
		return ls.creatIfNotExist(label);
	}

	public List<Filter> getAllFilters() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		LabelService ls = new LabelService();
		return ls.getAllFilters(user);
	}

	public Filter addFilter(Filter filter)
			throws NullPointerException, PropertyTooLongException,
			PropertyEmptyException, SecurityBreachException,
			PropertyValueException {
		if (filter == null) {
			throw new NullPointerException();
		}
		for (Filter f : filter.getFilterObjects()) {
			if (f == null) {
				throw new NullPointerException();
			}
		}

		if (!FilterType.TOP_LEVEL_FILTER.equals(filter.getType())) {
			String type =
				filter.getType() != null ? filter.getType().toString() : "null";
			throw new PropertyValueException("type", type,
				"nust be top level filter");
		}
		for (Filter f : filter.getFilterObjects()) {
			if (!FilterType.SECOND_LEVEL_FILTER.equals(f.getType())) {
				String type =
					f.getType() != null ? f.getType().toString() : "null";
				throw new PropertyValueException("type", type,
					"nust be second level filter");
			}
		}

		Sanitizer.checkUser("user", filter.getUser());
		Sanitizer.checkStringEmpty("name", filter.getName());
		Sanitizer.checkStringLength("name", filter.getName());

		LabelService ls = new LabelService();
		return ls.createFilter(filter);
	}

	public void updateFilter(Filter filter)
			throws NullPointerException, PropertyTooLongException,
			PropertyEmptyException, SecurityBreachException {
		if (filter == null) {
			throw new NullPointerException();
		}
		LabelService ls = new LabelService();
		Filter byKey = ls.getFilterByKey(filter.getKey());
		Sanitizer.checkUser("user", byKey.getUser());
		Sanitizer.checkStringEmpty("name", filter.getName());
		Sanitizer.checkStringLength("name", filter.getName());
		byKey.setName(filter.getName());
		ls.updateFilter(byKey);
	}
}
