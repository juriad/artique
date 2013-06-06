package cz.artique.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.Resources;

public class LoginPage extends Composite {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, LoginPage> {}

	static {
		Resources resources = GWT.create(Resources.class);
		resources.loginStyle().ensureInjected();
	}

	@UiField
	Anchor signIn;

	public LoginPage() {
		initWidget(uiBinder.createAndBindUi(this));
		signIn.setHref(ArtiqueWorld.WORLD.getUserInfo().getLoginUrl());
	}
}
