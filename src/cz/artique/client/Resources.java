package cz.artique.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface Resources extends ClientBundle {

	@NotStrict
	@Source("test-css.css")
	CssResource css();
}
