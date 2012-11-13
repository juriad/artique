package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.item.UserItem;

public interface ClientItemServiceAsync {

	void getItems(AsyncCallback<List<UserItem>> callback);

}
