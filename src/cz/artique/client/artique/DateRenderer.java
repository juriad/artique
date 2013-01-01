package cz.artique.client.artique;

import java.util.Date;

import com.google.code.gwteyecandy.Tooltip;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.i18n.ArtiqueMessages;
import cz.artique.client.settings.DateSettings;
import cz.artique.client.settings.DateSettings.DateShowOption;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;

public class DateRenderer extends AbstractSafeHtmlRenderer<UserItem> {

	interface TooltipTemplate extends SafeHtmlTemplates {
		@Template("<span class='{0}'>{1}</span>")
		SafeHtml tooltip(SafeHtml publishedMessage);
	}

	private static TooltipTemplate tooltipTemplate;

	private final DateSettings settings;

	public DateRenderer(DateSettings settings) {
		this.settings = settings;
		if (tooltipTemplate == null) {
			tooltipTemplate = GWT.create(TooltipTemplate.class);
		}
	}

	private String abbrDate(Date d) {
		// TODO abbrTime
		return settings.getAbbrFormat().format(d, settings.getTimeZone());
	}

	private String fullDate(Date d) {
		return settings.getFullFormat().format(d, settings.getTimeZone());
	}

	public SafeHtml render(UserItem object) {
		Item item = object.getItemObject();

		String addedAbbr = abbrDate(item.getAdded());
		String addedFull = fullDate(item.getAdded());
		String publishedAbbr = null;
		String publishedFull = null;

		if (item.getPublished() != null) {
			publishedAbbr = abbrDate(item.getPublished());
			publishedFull = fullDate(item.getPublished());
		}

		ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();

		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String time;
		if (DateShowOption.SHOW_PUBLISHED.equals(settings.getDateShowOption())
			&& item.getPublished() != null) {
			time = publishedAbbr;
			sb.append(tooltipTemplate.tooltip(messages.getDatePublished(
				publishedAbbr, publishedFull)));
			sb.append(tooltipTemplate.tooltip(messages.getDateAdded(addedAbbr,
				addedFull)));
		} else {
			time = addedAbbr;
			sb.append(tooltipTemplate.tooltip(messages.getDateAdded(addedAbbr,
				addedFull)));
			if (item.getPublished() != null) {
				sb.append(tooltipTemplate.tooltip(messages.getDatePublished(
					publishedAbbr, publishedFull)));
			}
		}

		Label l = new Label(time);

		Tooltip tip = new Tooltip();
		tip.setHtml(sb.toSafeHtml());
		tip.attachTo(l);

		String label = l.getElement().getString();
		return SafeHtmlUtils.fromTrustedString(label);
	}
}
