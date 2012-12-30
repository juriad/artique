package cz.artique.client.artique;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Anchor;

import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.source.UserSource;

public class UserSourceRenderer extends AbstractSafeHtmlRenderer<UserSource> {

	public SafeHtml render(final UserSource object) {
		if (object == null || object.getName().trim().isEmpty()) {
			return SafeHtmlUtils.fromString(ArtiqueI18n.I18N
				.getConstants()
				.missingContent());
		}

		SafeHtml name = SafeHtmlUtils.fromString(object.getName().trim());
		Anchor a = new Anchor(name);
		a.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListingHistory.HISTORY.showSource(object);
			}
		});

		String link = a.getElement().getString();
		return SafeHtmlUtils.fromTrustedString(link);
	}
}
