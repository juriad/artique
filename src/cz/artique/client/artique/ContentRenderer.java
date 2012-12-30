package cz.artique.client.artique;

import com.google.appengine.api.datastore.Text;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;

import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.item.ContentType;

public class ContentRenderer extends AbstractSafeHtmlRenderer<Text> {

	private ContentType type;

	public ContentRenderer(ContentType type) {
		this.type = type;
	}

	public SafeHtml render(Text object) {
		if (object == null || object.getValue() == null
			|| object.getValue().trim().isEmpty()) {
			return SafeHtmlUtils.fromString(ArtiqueI18n.I18N
				.getConstants()
				.missingContent());
		}
		String content = object.getValue().trim();
		if (ContentType.HTML.equals(type)) {
			return SafeHtmlUtils.fromTrustedString(content);
		} else {
			return SafeHtmlUtils.fromString(object.getValue());
		}
	}
}
