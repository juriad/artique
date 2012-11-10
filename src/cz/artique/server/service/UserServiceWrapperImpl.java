package cz.artique.server.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.artique.client.UserInfo;
import cz.artique.client.service.UserServiceWrapper;

public class UserServiceWrapperImpl extends RemoteServiceServlet
		implements UserServiceWrapper {

	private static final long serialVersionUID = 1L;
	final static public UserService userService = UserServiceFactory
		.getUserService();

	public UserInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserInfo loginInfo = new UserInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}
}
