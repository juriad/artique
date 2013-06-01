package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientLabelService;
import cz.artique.server.service.LabelService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.validation.ValidationException;

public class ClientLabelServiceImpl implements ClientLabelService {

	public List<Label> getAllLabels() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		LabelService ls = new LabelService();
		return ls.getAllLabels(user);
	}

	public Label addLabel(Label label) throws ValidationException {
		Validator<AddLabel> validator = new Validator<AddLabel>();
		validator.checkNullability(AddLabel.LABEL, false, label);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		label.setUser(user);
		label.setLabelType(LabelType.USER_DEFINED);
		label.setName(validator.checkLabel(AddLabel.NAME, label.getName()));

		LabelService ls = new LabelService();
		return ls.creatIfNotExist(label);
	}
}
