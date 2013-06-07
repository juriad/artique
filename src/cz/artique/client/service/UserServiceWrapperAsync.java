package cz.artique.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.user.UserInfo;

public interface UserServiceWrapperAsync {

	void login(String requestUri, AsyncCallback<UserInfo> callback);
}
