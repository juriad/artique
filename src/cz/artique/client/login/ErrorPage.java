package cz.artique.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueResources;

public class ErrorPage extends Composite {

	private static ErrorUiBinder uiBinder = GWT.create(ErrorUiBinder.class);

	interface ErrorUiBinder extends UiBinder<Widget, ErrorPage> {}

	static {
		ArtiqueResources resources = GWT.create(ArtiqueResources.class);
		resources.loginStyle().ensureInjected();
	}

	public ErrorPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
