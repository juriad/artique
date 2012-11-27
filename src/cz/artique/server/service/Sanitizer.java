package cz.artique.server.service;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyTooLongException;
import cz.artique.shared.utils.PropertyValueException;
import cz.artique.shared.utils.SecurityBreachException;

public class Sanitizer {
	private Sanitizer() {}

	public static void checkStringLength(String property, String value)
			throws PropertyTooLongException {
		if (value.length() > 500) {
			throw new PropertyTooLongException(property, value, 500);
		}
	}

	public static void checkStringEmpty(String property, String value)
			throws PropertyEmptyException {
		if (value == null || value.isEmpty()) {
			throw new PropertyEmptyException(property);
		}
	}

	public static void checkTextEmpty(String property, Text value)
			throws PropertyEmptyException {
		if (value == null || value.getValue() == null
			|| value.getValue().isEmpty()) {
			throw new PropertyEmptyException(property);
		}
	}

	public static void expectValue(String property, Object value,
			Object expected)
			throws PropertyValueException, PropertyEmptyException {
		if (expected == null) {
			if (value == null) {

			} else {
				throw new PropertyValueException(property, value.toString(),
					"this property must be null");
			}
		} else {
			if (value == null) {
				throw new PropertyEmptyException(property);
			} else {
				if (expected.equals(value)) {

				} else {
					throw new PropertyValueException(property,
						value.toString(), "expected value was "
							+ expected.toString());
				}
			}
		}
	}

	public static void checkUrl(String property, Link url)
			throws PropertyEmptyException, PropertyValueException {
		if (url == null || url.getValue() == null || url.getValue().isEmpty()) {
			throw new PropertyEmptyException(property);
		}
		if (checkLink(url) == null) {
			throw new PropertyValueException(property, url.getValue(),
				"malformed url");
		}
	}

	public static Link checkLink(Link url) {
		if (url == null) {
			return null;
		}
		try {
			new URI(url.getValue()); // test url
			return url;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public static void checkUser(String property, User user)
			throws SecurityBreachException {
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

	public static void checkPreserveKey(GenKey gen)
			throws SecurityBreachException {
		if (!ServerUtils.genKey(gen).equals(gen.getKey())) {
			throw new SecurityBreachException("Update may not change key");
		}
	}
}
