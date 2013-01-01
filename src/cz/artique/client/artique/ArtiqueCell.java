package cz.artique.client.artique;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.listing.InfiniteListCell;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;

public class ArtiqueCell extends InfiniteListCell<UserItem> {

	interface HeaderTemplate extends SafeHtmlTemplates {
		// @formatter:off
		@Template("<span class='source'>"
			+ "{0}"
			+ "</span>" 
			+ "<span class='labels'>" 
			+ "{1}" 
			+ "</span>"
			+ "<span class='title'>"
			+ "{2}"
			+ "</span>"
			+ "<span class='time'>"
			+ "{3}"
			+ "</span>")
		// @formatter:on
		SafeHtml header(SafeHtml source, SafeHtml labels, SafeHtml title,
				SafeHtml time);
	}

	private static HeaderTemplate headerTemplate;

	private ContentRenderer htmlContentRenderer = new ContentRenderer(
		ContentType.HTML);
	private ContentRenderer textContentRenderer = new ContentRenderer(
		ContentType.PLAIN_TEXT);
	private UserSourceRenderer userSourceRenderer = new UserSourceRenderer();
	private TitleRenderer titleRenderer = new TitleRenderer();

	public ArtiqueCell() {
		super();
		if (headerTemplate == null) {
			GWT.create(HeaderTemplate.class);
		}
	}

	@Override
	protected SafeHtml renderContent(UserItem e) {
		if (ContentType.HTML.equals(e.getItemObject().getContentType())) {
			return htmlContentRenderer.render(e.getItemObject().getContent());
		}
		return textContentRenderer.render(e.getItemObject().getContent());
	}

	@Override
	protected SafeHtml renderHeader(UserItem e) {
		UserSource us = ArtiqueWorld.WORLD.getUserSource(e.getUserSource());

		// TODO pokracovat zde
		// TODO label renderer, time renderer
		SafeHtml header =
			headerTemplate.header(userSourceRenderer.render(us), null,
				titleRenderer.render(e), null);
		return header;
	}
}
