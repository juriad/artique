package cz.artique.client.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.shortcut.Shortcut;

public interface ClientShortcutServiceAsync {

	void createShortcut(Shortcut shortcut, AsyncCallback<Shortcut> callback);

	void deleteShortcut(Key shortcutKey, AsyncCallback<Void> callback);

	void getAllShortcuts(AsyncCallback<List<Shortcut>> callback);

}
