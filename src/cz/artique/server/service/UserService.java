package cz.artique.server.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.crawler.CrawlerUtils;
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
		ui.setClientToken(genClientToken());
		ui.setKey(KeyGen.genKey(ui));
		Datastore.put(ui);
		return ui;
	}

	private String genClientToken() {
		long date = new Date().getTime();
		int i = new Random().nextInt();
		String token = date + "$" + i;
		String sha1 = CrawlerUtils.toSHA1(token);

		UserInfo existing;
		do {
			existing = getUserInfoByClientToken(sha1);
		} while (existing != null);
		return sha1;
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

	public UserInfo getUserInfoByClientToken(String clientToken) {
		UserInfoMeta meta = UserInfoMeta.get();
		List<UserInfo> asList =
			Datastore
				.query(meta)
				.filter(meta.clientToken.equal(clientToken))
				.asList();
		if (asList.isEmpty()) {
			return null;
		} else {
			return asList.get(0);
		}
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
}
