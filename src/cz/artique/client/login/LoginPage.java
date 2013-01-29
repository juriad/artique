package cz.artique.client.login;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.artique.client.ArtiqueWorld;

public class LoginPage extends Composite {
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
		"Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");

	public LoginPage() {
		initWidget(loginPanel);
		signInLink.setHref(ArtiqueWorld.WORLD.getUserInfo().getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
	}
}
