package cz.artique.client.login;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface LoginResources extends ClientBundle {
	@NotStrict
	@Source("login-style.css")
	CssResource loginStyle();
}
