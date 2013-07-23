package cz.artique.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@NotStrict
	@Source("style.css")
	CssResource style();

	@NotStrict
	@Source("dialogs.css")
	CssResource dialogs();

	@NotStrict
	@Source("login/login-style.css")
	CssResource loginStyle();

	@Source("icons/edit-find.png")
	ImageResource detail();

	@Source("icons/document-new.png")
	ImageResource createNew();

	@Source("icons/document-save-as.png")
	ImageResource saveCurrent();

	@Source("icons/edit-clear.png")
	ImageResource clear();

	@Source("icons/mail-mark-not-junk.png")
	ImageResource hideDisabled();

	@Source("icons/mail-mark-junk.png")
	ImageResource showDisabled();

	@Source("icons/reload.png")
	ImageResource reload();

	@Source("icons/add.png")
	ImageResource add();

	@Source("mainBackground.png")
	ImageResource mainBackground();

}
