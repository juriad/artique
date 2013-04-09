package cz.artique.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@NotStrict
	@Source("test-css.css")
	CssResource css();

	@Source("icons/edit-find.png")
	ImageResource detail();

	@Source("icons/document-new.png")
	ImageResource createNew();

	@Source("icons/document-save-as.png")
	ImageResource saveCurrent();

}
