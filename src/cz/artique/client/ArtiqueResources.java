package cz.artique.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface ArtiqueResources extends ClientBundle {

	@NotStrict
	@Source("Artique.css")
	CssResource style();

	@NotStrict
	@Source("login/login-style.css")
	CssResource loginStyle();

	@Source("mainBackground.png")
	ImageResource mainBackground();

}
