package cz.artique.client.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientShortcutService extends RemoteService {
	public enum GetAllShortcuts implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllShortcuts";
		}
	}

	List<Shortcut> getAllShortcuts();

	public enum CreateShortcut implements HasIssue {
		SHORTCUT,
		KEY_STROKE,
		TYPE,
		GENERAL;
		public String enumName() {
			return "CreateShortcut";
		}
	}

	Shortcut createShortcut(Shortcut shortcut) throws ValidationException;

	public enum DeleteShortcut implements HasIssue {
		SHORTCUT,
		GENERAL;
		public String enumName() {
			return "DeleteShortcut";
		}
	}

	void deleteShortcut(Key shortcutKey) throws ValidationException;
}
