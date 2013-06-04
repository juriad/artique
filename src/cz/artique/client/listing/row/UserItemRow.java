package cz.artique.client.listing.row;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listing.ListingConstants;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;

public class UserItemRow extends RowWidget {

	public static final UserItemRowFactory FACTORY = new UserItemRowFactory();

	public static class UserItemRowFactory {
		public RowWidget createWidget(UserItem data) {
			return new UserItemRow(data);
		}
	}

	private UserItemLabelsBar labels;
	private FlowPanel header;

	private Label title;
	private HTMLPanel content;
	private Label source;
	private Label added;

	public UserItemRow(UserItem data) {
		super(data);

		header = new FlowPanel();
		labels = new UserItemLabelsBar();
		header.add(labels);

		Anchor link = createLinkAnchor();
		header.add(link);

		source = createSourceLabel();
		header.add(source);

		added = createDateLabel();
		header.add(added);

		title = new Label(getValue().getItemObject().getTitle());
		title.setStylePrimaryName("itemTitle");
		header.add(title);

		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (event.isControlKeyDown()) {
					openOriginal();
				} else {
					toggleExpanded();
				}
			}
		});

		setHeader(header);

		SafeHtml contentHTML = SafeHtmlUtils.EMPTY_SAFE_HTML;
		Text contentText = getValue().getItemObject().getContent();
		if (contentText != null && contentText.getValue() != null) {
			ContentType contentType =
				getValue().getItemObject().getContentType();
			if (contentType != null) {
				switch (contentType) {
				case HTML:
					contentHTML =
						SafeHtmlUtils.fromTrustedString(contentText.getValue());
					break;
				case PLAIN_TEXT:
					contentHTML =
						SafeHtmlUtils.fromString(contentText.getValue());
					break;
				default:
					break;
				}
			}
		} else {
			I18n.getListingConstants().missingContent();
		}

		content = new HTMLPanel(contentHTML);
		setContent(content);

		addOpenHandler(new OpenHandler<RowWidget>() {
			public void onOpen(OpenEvent<RowWidget> event) {
				Managers.ITEMS_MANAGER.readSet(getValue(), true, null);
				setReadState();
			}
		});

		refresh();
	}

	private Anchor createLinkAnchor() {
		Image open = new Image(ArtiqueWorld.WORLD.getResources().open());
		Anchor link = new Anchor();
		link.setStylePrimaryName("itemLink");
		link.getElement().appendChild(open.getElement());
		link.setHref(getValue().getItemObject().getUrl().getValue());
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				openOriginal();
				event.preventDefault();
			}
		});
		return link;
	}

	public void refresh() {
		labels.setNewData(getValue());
		setReadState();
	}

	private void setReadState() {
		boolean read = getValue().isRead();
		setStyleDependentName("read", read);
		setStyleDependentName("unread", !read);
	}

	public void openOriginal() {
		Window.open(getValue().getItemObject().getUrl().getValue(), "_blank",
			"");
	}

	public void openAddLabel() {
		labels.openSuggestion();
	}

	private Label createDateLabel() {
		ListingConstants constants = I18n.getListingConstants();

		DateTimeFormatRenderer shortRenderer =
			new DateTimeFormatRenderer(
				DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT));

		Item itemObject = getValue().getItemObject();
		String simple;
		if (itemObject.getPublished() != null) {
			simple = shortRenderer.render(itemObject.getPublished());
		} else {
			simple = shortRenderer.render(itemObject.getAdded());
		}
		Label label = new Label(simple);

		DateTimeFormatRenderer longRenderer =
			new DateTimeFormatRenderer(
				DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_LONG));
		String full = "";
		full +=
			constants.added() + ": "
				+ longRenderer.render(itemObject.getAdded());
		if (itemObject.getPublished() != null) {
			full +=
				"\n" + constants.published() + ": "
					+ longRenderer.render(itemObject.getPublished());
		}
		label.setTitle(full);

		label.setStylePrimaryName("itemDate");
		return label;
	}

	private Label createSourceLabel() {
		Key userSourceKey = getValue().getUserSource();
		UserSource userSource =
			Managers.SOURCES_MANAGER.getSourceByKey(userSourceKey);
		Label label = new Label(userSource.getName());
		label.setStylePrimaryName("itemSource");

		Link url = userSource.getSourceObject().getUrl();
		if (url != null && url.getValue() != null) {
			label.setTitle(url.getValue());
		}
		return label;
	}
}
