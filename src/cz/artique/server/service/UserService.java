package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.meta.user.UserInfoMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.user.UserInfo;

public class UserService {
	public UserService() {}

	public UserInfo getUserInfo(String userId) {
		UserInfo ui = new UserInfo();
		ui.setUserId(userId);
		Key key = KeyGen.genKey(ui);
		UserInfo userInfo = Datastore.getOrNull(UserInfoMeta.get(), key);
		return userInfo;
	}

	public UserInfo createUserInfo(User user) {
		UserInfo ui = new UserInfo();
		ui.setUserId(user.getUserId());
		ui.setNickname(user.getNickname());
		Datastore.put(ui);
		return ui;
	}

	public static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}

	public static String getCurrentUserId() {
		return UserServiceFactory.getUserService().getCurrentUser().getUserId();
	}

	public static String createLogoutURL(String requestUri) {
		return UserServiceFactory.getUserService().createLogoutURL(requestUri);
	}

	public static String createLoginURL(String requestUri) {
		return UserServiceFactory.getUserService().createLoginURL(requestUri);
	}

	public UserInfo getUserInfoByNickname(String nickname) {
		UserInfoMeta meta = UserInfoMeta.get();
		List<UserInfo> asList =
			Datastore
				.query(meta)
				.filter(meta.nickname.equal(nickname))
				.asList();
		if (asList.isEmpty()) {
			return null;
		} else {
			return asList.get(0);
		}
	}
}
