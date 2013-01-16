package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientLabelService;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyTooLongException;
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
}
