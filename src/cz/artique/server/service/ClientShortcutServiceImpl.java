package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientShortcutService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

public class ClientShortcutServiceImpl implements ClientShortcutService {

	public List<Shortcut> getAllShortcuts() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ShortcutService ss = new ShortcutService();
		return ss.getAllShortcuts(user);
	}

	public Shortcut createShortcut(Shortcut shortcut)
			throws ValidationException {
		Validator<CreateShortcut> validator = new Validator<CreateShortcut>();
		validator.checkNullability(CreateShortcut.SHORTCUT, false, shortcut);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		shortcut.setUser(user);
		validator.checkNullability(CreateShortcut.KEY_STROKE, false,
			shortcut.getKeyStroke());
		if (shortcut.getKeyStroke().length() > 500) {
			throw new ValidationException(new Issue<CreateShortcut>(
				CreateShortcut.KEY_STROKE, IssueType.TOO_LONG));
		}

		validator.checkNullability(CreateShortcut.TYPE, false,
			shortcut.getType());
		ShortcutService ss = new ShortcutService();
		ss.createShortcut(shortcut);
		return shortcut;
	}

	public void deleteShortcut(Key shortcutKey) throws ValidationException {
		Validator<CreateShortcut> validator = new Validator<CreateShortcut>();
		validator.checkNullability(CreateShortcut.SHORTCUT, false, shortcutKey);
		ShortcutService ss = new ShortcutService();
		Shortcut shortcutByKey = ss.getShortcutByKey(shortcutKey);
		validator.checkNullability(CreateShortcut.SHORTCUT, false,
			shortcutByKey);
		User user = UserServiceFactory.getUserService().getCurrentUser();
		validator.checkUser(CreateShortcut.SHORTCUT, user,
			shortcutByKey.getUser());
		ss.deleteShortcut(shortcutKey);
	}

}
