package cz.artique.client.artique;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;

import cz.artique.client.i18n.ArtiqueI18n;

public class TitleRenderer extends AbstractSafeHtmlRenderer<String> {

	public TitleRenderer() {}

	public SafeHtml render(String object) {
		if (object == null || object.trim().isEmpty()) {
			return SafeHtmlUtils.fromString(ArtiqueI18n.I18N
				.getConstants()
				.missingTitle());
		}
		return SafeHtmlUtils.fromTrustedString(object.trim());
	}
}
