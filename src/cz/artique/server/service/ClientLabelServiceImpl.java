package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientLabelService;
import cz.artique.shared.model.label.Label;

public class ClientLabelServiceImpl implements ClientLabelService {

	public List<Label> getAllLabels() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		LabelService ls = new LabelService();
		return ls.getAllLabels(user);
	}

	public Label addLabel(Label label) {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		LabelService ls = new LabelService();
		label.setUser(user);
		return ls.creatIfNotExist(label);
	}

}
