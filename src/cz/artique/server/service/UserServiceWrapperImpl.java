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
		UserInfo userInfo = new UserInfo();

		if (user != null) {
			userInfo.setUser(user);
			userInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			ensureManualSource();
		} else {
			userInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return userInfo;
	}

	private void ensureManualSource() {
		UserSourceService uss = new UserSourceService();
		uss.ensureManualSource();
	}
}
