package cz.artique.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.service.UserServiceWrapper;
import cz.artique.client.service.UserServiceWrapperAsync;

public class Main implements EntryPoint {

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
		"Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");

	public void onModuleLoad() {
		UserServiceWrapperAsync userService =
			GWT.create(UserServiceWrapper.class);
		System.out.println("start");

		userService.login(GWT.getHostPageBaseURL(),
			new AsyncCallback<UserInfo>() {
				public void onFailure(Throwable error) {}

				public void onSuccess(UserInfo result) {
					ArtiqueWorld.WORLD.setUserInfo(result);
					if (result.getUser() != null) {
						loadArtique();
					} else {
						loadLogin();
					}
				}
			});
	}

	protected void loadArtique() {

		Test1 t = new Test1();
		RootLayoutPanel.get().add(t);
	}

	private void loadLogin() {
		signInLink.setHref(ArtiqueWorld.WORLD.getUserInfo().getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get().add(loginPanel);
	}

}
