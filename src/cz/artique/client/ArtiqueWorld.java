package cz.artique.client;

import com.google.appengine.api.users.User;

public enum ArtiqueWorld {
	WORLD;

	private UserInfo userInfo;
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public User getUser() {
		return getUserInfo().getUser();
	}
}
