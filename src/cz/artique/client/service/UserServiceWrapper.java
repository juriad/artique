package cz.artique.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.client.UserInfo;

@RemoteServiceRelativePath("userService")
public interface UserServiceWrapper extends RemoteService {
	public UserInfo login(String requestUri);
}
