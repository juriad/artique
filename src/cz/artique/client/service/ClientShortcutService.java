package cz.artique.client.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

/**
 * The service responsible for {@link Shortcut}s passing and related
 * operations between client and server.
 * 
 * @author Adam Juraszek
 * 
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientShortcutService extends RemoteService {
	public enum GetAllShortcuts implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllShortcuts";
		}
	}

	/**
	 * Gets list of all existing {@link Shortcut}s for current user.
	 * 
	 * @return list of all {@link Shortcut}s
	 */
	List<Shortcut> getAllShortcuts();

	public enum CreateShortcut implements HasIssue {
		SHORTCUT,
		KEY_STROKE,
		TYPE,
		ACTION,
		REFERENCED,
		GENERAL;
		public String enumName() {
			return "CreateShortcut";
		}
	}

	/**
	 * Creates a new {@link Shortcut} for current user.
	 * 
	 * @param shortcut
	 *            {@link Shortcut} to be created
	 * @return created {@link Shortcut}
	 * @throws ValidationException
	 *             if validation of the {@link Shortcut} fails
	 */
	Shortcut createShortcut(Shortcut shortcut) throws ValidationException;

	public enum DeleteShortcut implements HasIssue {
		SHORTCUT,
		GENERAL;
		public String enumName() {
			return "DeleteShortcut";
		}
	}

	/**
	 * Deletes existing {@link Shortcut}.
	 * 
	 * @param shortcutKey
	 *            key of {@link Shortcut} to be deleted
	 * @throws ValidationException
	 *             if validation of the {@link Shortcut} fails
	 */
	void deleteShortcut(Key shortcutKey) throws ValidationException;
}
