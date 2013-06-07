package cz.artique.server.service;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.artique.client.service.UserServiceWrapper;
import cz.artique.shared.model.user.UserInfo;

public class UserServiceWrapperImpl extends RemoteServiceServlet
		implements UserServiceWrapper {

	private static final long serialVersionUID = 1L;

	public UserInfo login(String requestUri) {
		UserService us = new UserService();
		User user = UserService.getCurrentUser();
		UserInfo info;
		if (user != null) {
			info = us.getUserInfo(user.getUserId());
			if (info == null) {
				info = us.createUserInfo(user);
			}
			info.setLogoutUrl(UserService.createLogoutURL(requestUri));
			UserSourceService uss = new UserSourceService();
			uss.getManualUserSource(user.getUserId());
		} else {
			info = new UserInfo();
			info.setLoginUrl(UserService.createLoginURL(requestUri));
		}
		return info;
	}
}
