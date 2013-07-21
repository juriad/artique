package cz.artique.client.listing.row;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listing.ListingConstants;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;

public class UserItemRow extends RowWidget {

	private static UserItemRowUiBinder uiBinder = GWT
		.create(UserItemRowUiBinder.class);

	interface UserItemRowUiBinder extends UiBinder<Widget, UserItemRow> {}

	public static final UserItemRowFactory FACTORY = new UserItemRowFactory();

	public static class UserItemRowFactory {
		public RowWidget createWidget(UserItem data) {
			return new UserItemRow(data);
		}
	}

	@UiField
	VerticalPanel row;

	@UiField
	FlowPanel header;

	@UiField
	UserItemLabelsBar labels;

	@UiField
	Anchor link;

	@UiField
	Label source;

	@UiField
	Label added;

	@UiField
	Anchor backup;

	@UiField
	Label title;

	@UiField
	FlowPanel content;

	private Image backupImage;

	public UserItemRow(UserItem data) {
		super(data);

		initWidget(uiBinder.createAndBindUi(this));

		fillLinkAnchor();
		fillSourceLabel();
		fillDateLabel();
		fillBackupAnchor();

		fillTitle();

		fillContent();
		setContent(content);

		addOpenHandler(new OpenHandler<RowWidget>() {
			public void onOpen(OpenEvent<RowWidget> event) {
				Managers.ITEMS_MANAGER.readSet(getValue(), true, null);
				setReadState();
			}
		});

		refresh();
	}

	private void fillLinkAnchor() {
		Image open = new Image(ArtiqueWorld.WORLD.getResources().open());
		link.getElement().appendChild(open.getElement());
		link.setHref(getValue().getItemObject().getUrl().getValue());
		link.setStyleName("itemIcon", true);
	}

	private void fillSourceLabel() {
		Key userSourceKey = getValue().getUserSource();
		UserSource userSource =
			Managers.SOURCES_MANAGER.getSourceByKey(userSourceKey);
		source.setText(userSource.getName());

		Link url = userSource.getSourceObject().getUrl();
		if (url != null && url.getValue() != null) {
			source.setTitle(url.getValue());
		} else if(SourceType.MANUAL.equals(userSource.getSourceType())) {
			source.setTitle(I18n.getListingConstants().manualSourceTitle());
		}
	}

	private void fillDateLabel() {
		ListingConstants constants = I18n.getListingConstants();

		DateTimeFormatRenderer shortRenderer =
			new DateTimeFormatRenderer(
				DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT));

		Item itemObject = getValue().getItemObject();
		String simple;
		if (itemObject instanceof ArticleItem
			&& ((ArticleItem) itemObject).getPublished() != null) {
			simple =
				shortRenderer.render(((ArticleItem) itemObject).getPublished());
		} else {
			simple = shortRenderer.render(itemObject.getAdded());
		}
		added.setText(simple);

		DateTimeFormatRenderer longRenderer =
			new DateTimeFormatRenderer(
				DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_LONG));

		// TODO nice to have: universal line separator in tooltip
		String full = "";
		full +=
			constants.added() + ": "
				+ longRenderer.render(itemObject.getAdded());
		if (itemObject instanceof ArticleItem
			&& ((ArticleItem) itemObject).getPublished() != null) {
			full +=
				"\n"
					+ constants.published()
					+ ": "
					+ longRenderer.render(((ArticleItem) itemObject)
						.getPublished());
		}
		if (itemObject instanceof PageChangeItem) {
			Date comparedTo = ((PageChangeItem) itemObject).getComparedTo();
			if (comparedTo != null) {
				full +=
					"\n" + constants.comparedTo() + ": "
						+ longRenderer.render(comparedTo);
			}
		}
		added.setTitle(full);
	}

	private void fillBackupAnchor() {
		backup.setStyleName("itemIcon", true);
	}

	private void fillTitle() {
		Item itemObject = getValue().getItemObject();
		title.setText(itemObject.getTitle());

		if (itemObject instanceof ArticleItem) {
			String author = ((ArticleItem) itemObject).getAuthor();
			if (author != null && !author.trim().isEmpty()) {
				ListingConstants constants = I18n.getListingConstants();
				title.setTitle(constants.author() + ": " + author);
			}
		}

		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (event.isControlKeyDown()) {
					openOriginal();
				} else {
					toggleExpanded();
				}
			}
		});
	}

	private void fillContent() {
		Item itemObject = getValue().getItemObject();

		SafeHtml contentHTML = SafeHtmlUtils.EMPTY_SAFE_HTML;
		Text contentText = itemObject.getContent();
		if (contentText != null && contentText.getValue() != null) {
			ContentType contentType =
				getValue().getItemObject().getContentType();
			if (contentType != null) {
				switch (contentType) {
				case HTML:
					String htmlValue = contentText.getValue();
					// target all links to a new tab
					htmlValue =
						htmlValue.replaceAll("<a[\\s>]", "<a target='_blank' ");
					contentHTML = SafeHtmlUtils.fromTrustedString(htmlValue);
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
			contentHTML =
				SafeHtmlUtils.fromString(I18n
					.getListingConstants()
					.missingContent());
		}

		if (itemObject instanceof PageChangeItem) {
			PageChangeItem change = (PageChangeItem) itemObject;
			SafeHtml diffHTML = SafeHtmlUtils.EMPTY_SAFE_HTML;
			Text diffText = change.getDiff();
			if (diffText != null && diffText.getValue() != null) {
				ContentType contentType =
					getValue().getItemObject().getContentType();
				if (contentType != null) {
					switch (contentType) {
					case HTML:
						String htmlValue = diffText.getValue();
						// target all links to a new tab
						htmlValue =
							htmlValue.replaceAll("<a[\\s>]",
								"<a target='_blank' ");
						diffHTML = SafeHtmlUtils.fromTrustedString(htmlValue);
						break;
					case PLAIN_TEXT:
						diffHTML =
							SafeHtmlUtils.fromString(diffText.getValue());
						break;
					default:
						break;
					}
				}
			} else {
				diffHTML =
					SafeHtmlUtils.fromString(I18n
						.getListingConstants()
						.missingContent());
			}

			ListingConstants constants = I18n.getListingConstants();
			TabLayoutPanel p = new TabLayoutPanel(1.5, Unit.EM);
			p.add(new HTML(contentHTML), constants.newContent());
			p.add(new HTML(diffHTML), constants.diffContent());
		} else {
			content.add(new HTML(contentHTML));
		}
	}

	public void refresh() {
		labels.setNewData(getValue());
		setReadState();
		if (backupImage == null && getValue().getBackupBlobKey() != null) {
			backupImage = new Image(ArtiqueWorld.WORLD.getResources().backup());
			backup.getElement().appendChild(backupImage.getElement());
			String blobKey = getValue().getBackupBlobKey();
			backup.setHref("/export/backupService?backup=" + blobKey);
		}
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
}
