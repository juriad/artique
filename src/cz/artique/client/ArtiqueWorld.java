package cz.artique.client;

import com.google.appengine.api.users.User;

import cz.artique.shared.model.label.ListFilter;

public enum ArtiqueWorld {
	WORLD;

	private UserInfo userInfo;
	private ListFilter currentListFilter;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public User getUser() {
		return getUserInfo().getUser();
	}

	public void setCurrentListFilter(ListFilter current) {
		currentListFilter = current;
	}

	public ListFilter getCurrentListFilter() {
		return currentListFilter;
	}
}
