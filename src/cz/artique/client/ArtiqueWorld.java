package cz.artique.client;

import com.google.appengine.api.users.User;

import cz.artique.client.artiqueListing.ArtiqueList;

public enum ArtiqueWorld {
	WORLD;

	private UserInfo userInfo;
	private ArtiqueList list;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public User getUser() {
		return getUserInfo().getUser();
	}

	public ArtiqueList getList() {
		return list;
	}

	public void setList(ArtiqueList list) {
		this.list = list;
	}

}
