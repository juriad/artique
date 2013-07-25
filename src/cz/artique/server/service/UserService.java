package cz.artique.server.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.meta.user.UserInfoMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.user.UserInfo;

/**
 * Hides {@link com.google.appengine.api.users.UserService}; provides most of
 * its functionality.
 * The aim of this service is to identify user by persistent ID and option to
 * search for user by his ID, nickname or clietnToken.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserService {
	public UserService() {}

	/**
	 * Gets {@link UserInfo} by his ID.
	 * 
	 * @param userId
	 *            ID of user
	 * @return {@link UserInfo}
	 */
	public UserInfo getUserInfo(String userId) {
		UserInfo ui = new UserInfo();
		ui.setUserId(userId);
		Key key = KeyGen.genKey(ui);
		UserInfo userInfo = Datastore.getOrNull(UserInfoMeta.get(), key);
		return userInfo;
	}

	/**
	 * Creates {@link UserInfo} for a new {@link User}.
	 * 
	 * @param user
	 *            AppEngine {@link User} the info is created for
	 * @return created {@link UserInfo}
	 */
	public UserInfo createUserInfo(User user) {
		UserInfo ui = new UserInfo();
		ui.setUserId(user.getUserId());
		ui.setNickname(user.getNickname());
		ui.setClientToken(genClientToken());
		ui.setKey(KeyGen.genKey(ui));
		Datastore.put(ui);
		return ui;
	}

	/**
	 * @return new unique random client token
	 */
	private String genClientToken() {
		long date = new Date().getTime();
		int i = new Random().nextInt();
		String token = date + "$" + i;
		String sha1 = ServerUtils.toSHA1(token);

		UserInfo existing;
		do {
			existing = getUserInfoByClientToken(sha1);
		} while (existing != null);
		return sha1;
	}

	/**
	 * Finds {@link UserInfo} by his nickname.
	 * 
	 * @param nickname
	 *            nickname of user
	 * @return {@link UserInfo} or null if not found
	 */
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

	/**
	 * Finds {@link UserInfo} by his client token.
	 * This method allows to pretend the user is logged in when client extension
	 * makes a request.
	 * 
	 * @param clientToken
	 *            client token of user
	 * @return {@link UserInfo} or null if not found
	 */
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

	/**
	 * Delegates to AppEngine {@link com.google.appengine.api.users.UserService}
	 * .
	 * 
	 * @see com.google.appengine.api.users.UserService#getCurrentUser()
	 * @return current AppEngine {@link User}
	 */
	public static User getCurrentUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}

	/**
	 * Delegates to AppEngine {@link com.google.appengine.api.users.User} .
	 * 
	 * @see com.google.appengine.api.users.User#getUserId()
	 * @see #getCurrentUser()
	 * @return ID of current AppEngine {@link User}
	 */
	public static String getCurrentUserId() {
		return UserServiceFactory.getUserService().getCurrentUser().getUserId();
	}

	/**
	 * Delegates to AppEngine {@link com.google.appengine.api.users.UserService}
	 * .
	 * 
	 * @see com.google.appengine.api.users.UserService#createLogoutURL(String)
	 * @param requestUri
	 *            requestUri
	 * @return logout URL
	 */
	public static String createLogoutURL(String requestUri) {
		return UserServiceFactory.getUserService().createLogoutURL(requestUri);
	}

	/**
	 * Delegates to AppEngine {@link com.google.appengine.api.users.UserService}
	 * .
	 * 
	 * @see com.google.appengine.api.users.UserService#createLoginURL(String)
	 * @param requestUri
	 *            requestUri
	 * @return login URL
	 */
	public static String createLoginURL(String requestUri) {
		return UserServiceFactory.getUserService().createLoginURL(requestUri);
	}
}
