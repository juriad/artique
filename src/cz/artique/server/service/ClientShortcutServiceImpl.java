package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.service.ClientShortcutService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

public class ClientShortcutServiceImpl implements ClientShortcutService {

	public List<Shortcut> getAllShortcuts() {
		String userId = UserService.getCurrentUserId();
		ShortcutService ss = new ShortcutService();
		return ss.getAllShortcuts(userId);
	}

	public Shortcut createShortcut(Shortcut shortcut)
			throws ValidationException {
		Validator<CreateShortcut> validator = new Validator<CreateShortcut>();
		validator.checkNullability(CreateShortcut.SHORTCUT, false, shortcut);
		String userId = UserService.getCurrentUserId();
		shortcut.setUserId(userId);
		if (shortcut.getKeyStroke() == null
			|| shortcut.getKeyStroke().isEmpty()) {
			throw new ValidationException(new Issue<CreateShortcut>(
				CreateShortcut.KEY_STROKE, IssueType.EMPTY_OR_NULL));
		}
		if (shortcut.getKeyStroke().length() > 500) {
			throw new ValidationException(new Issue<CreateShortcut>(
				CreateShortcut.KEY_STROKE, IssueType.TOO_LONG));
		}

		validator.checkNullability(CreateShortcut.TYPE, false,
			shortcut.getType());
		switch (shortcut.getType()) {
		case ACTION:
			validator.checkNullability(CreateShortcut.ACTION, false,
				shortcut.getAction());
			break;
		case LABEL:
		case LIST_FILTER:
			validator.checkNullability(CreateShortcut.REFERENCED, false,
				shortcut.getReferenced());
			break;
		default:
			throw new ValidationException(new Issue<CreateShortcut>(
				CreateShortcut.TYPE, IssueType.INVALID_VALUE));
		}

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
		String userId = UserService.getCurrentUserId();
		validator.checkUser(CreateShortcut.SHORTCUT, userId,
			shortcutByKey.getUserId());
		ss.deleteShortcut(shortcutKey);
	}

}
