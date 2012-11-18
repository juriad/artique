package cz.artique.server.service;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyTooLongException;
import cz.artique.shared.utils.SecurityBreachException;

public class Sanitizer {
	private Sanitizer() {}

	public static void checkStringLength(String property, String value) {
		if (value.length() > 500) {
			throw new PropertyTooLongException(property, value, 500);
		}
	}

	public static void checkStringEmpty(String property, String value) {
		if (value == null || value.isEmpty()) {
			throw new PropertyEmptyException(property);
		}
	}

	public static void checkTextEmpty(String property, Text value) {
		if (value == null || value.getValue() == null
			|| value.getValue().isEmpty()) {
			throw new PropertyEmptyException(property);
		}
	}

	public static void checkUrl(String property, Link url) {
		if (url == null || url.getValue() == null || url.getValue().isEmpty()) {
			throw new PropertyEmptyException(property);
		}
	}

	public static void checkUser(String property, User user) {
		if (!UserServiceFactory.getUserService().getCurrentUser().equals(user)) {
			throw new SecurityBreachException();
		}
	}

	public static Text trimText(Text text) {
		if (text != null && text.getValue() != null) {
			if (text.getValue().length() > 1000 * 1000) {
				return new Text(text.getValue().substring(0, 1000 * 1000));
			}
		}
		return text;
	}

	public static void checkPreserveKey(GenKey gen) {
		if (!ServerUtils.genKey(gen).equals(gen.getKey())) {
			throw new SecurityBreachException("Update may not change key");
		}
	}
}
