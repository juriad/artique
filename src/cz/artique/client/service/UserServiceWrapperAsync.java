package cz.artique.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.UserInfo;

public interface UserServiceWrapperAsync {

    void login(String requestUri, AsyncCallback<UserInfo> callback);
}
