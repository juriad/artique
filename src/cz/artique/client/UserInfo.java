package cz.artique.client;

import java.io.Serializable;

import com.google.appengine.api.users.User;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String loginUrl;
	private String logoutUrl;
	private User user;

	public UserInfo() {}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
