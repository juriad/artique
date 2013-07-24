package cz.artique.client.hierarchy.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

interface HierarchyResources extends ClientBundle {
	HierarchyResources INSTANCE = GWT.create(HierarchyResources.class);

	@NotStrict
	@Source("Hierarchy.css")
	CssResource style();

	@Source("../../icons/edit-find.png")
	ImageResource detail();

	@Source("../../icons/document-new.png")
	ImageResource createNew();

	@Source("../../icons/document-save-as.png")
	ImageResource saveCurrent();

	@Source("../../icons/edit-clear.png")
	ImageResource clear();

	@Source("../../icons/mail-mark-not-junk.png")
	ImageResource hideDisabled();

	@Source("../../icons/mail-mark-junk.png")
	ImageResource showDisabled();

	@Source("../../icons/reload.png")
	ImageResource reload();

	@Source("../../icons/add.png")
	ImageResource add();
}
