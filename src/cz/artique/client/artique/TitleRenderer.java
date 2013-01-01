package cz.artique.client.artique;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;

import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;

public class TitleRenderer extends AbstractSafeHtmlRenderer<UserItem> {

	interface TitleTemplate extends SafeHtmlTemplates {

		@Template("<a href='{0}' target='_blank'>{1}</a>")
		SafeHtml title(String url, String text);
	}

	private static TitleTemplate titleTemplate;

	public TitleRenderer() {
		if (titleTemplate == null) {
			titleTemplate = GWT.create(TitleTemplate.class);
		}
	}

	public SafeHtml render(UserItem object) {
		Item item = object.getItemObject();
		if (item == null || item.getTitle() == null
			|| item.getTitle().trim().isEmpty()) {
			return SafeHtmlUtils.fromString(ArtiqueI18n.I18N
				.getConstants()
				.missingTitle());
		}
		String title = item.getTitle().trim();

		if (item.getUrl() == null || item.getUrl().getValue() == null
			|| item.getUrl().getValue().trim().isEmpty()) {
			// no link
			return SafeHtmlUtils.fromString(title);
		}

		return titleTemplate.title(item.getUrl().getValue().trim(), title);
	}
}
