package cz.artique.client.listing.row;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.common.ContentsPanel;
import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listing.ListingConstants;
import cz.artique.client.manager.Managers;
import cz.artique.client.utils.ClientTextContent;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.PageChangeItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;
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

	public static class UserItemContentFactory
			implements ContentsPanel.ContentButtonFactory<Anchor> {

		public Anchor createButton(String name) {
			ListingConstants constants = I18n.getListingConstants();
			String title = constants.getString("content_" + name);
			return new Anchor(title);
		}
	}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("UserItemRow.css")
		CssResource style();

		@Source("../../icons/go-jump.png")
		ImageResource open();

		@Source("../../icons/lock.png")
		ImageResource backup();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

	@UiField
	VerticalPanel row;

	@UiField
	FlowPanel header;

	@UiField
	UserItemLabelsBar labels;

	@UiField
	Anchor link;

	@UiField
	Anchor source;

	@UiField
	Label added;

	@UiField
	Anchor backup;

	@UiField
	Label title;

	@UiField(provided = true)
	ContentsPanel<Anchor> content;

	private Image backupImage;

	public UserItemRow(UserItem data) {
		super(data);
		res.style().ensureInjected();
		content = new ContentsPanel<Anchor>(new UserItemContentFactory());

		initWidget(uiBinder.createAndBindUi(this));

		fillLinkAnchor();
		fillSourceLabel();
		fillDateLabel();
		fillBackupAnchor();

		fillTitle();

		setContent(content);

		addOpenHandler(new OpenHandler<RowWidget>() {
			public void onOpen(OpenEvent<RowWidget> event) {
				Managers.ITEMS_MANAGER.readSet(getValue(), true, null);
				setReadState();
				fillContent();
			}
		});

		refresh();
	}

	private void fillLinkAnchor() {
		Image open = new Image(res.open());
		link.getElement().appendChild(open.getElement());
		link.setHref(getValue().getItemObject().getUrl().getValue());
		link.setStyleName("itemIcon", true);
	}

	private void fillSourceLabel() {
		Key userSourceKey = getValue().getUserSource();
		UserSource userSource =
			Managers.SOURCES_MANAGER.getSourceByKey(userSourceKey);
		source.setText(userSource.getName());

		cz.artique.shared.model.label.Label label =
			Managers.LABELS_MANAGER.getLabelByKey(userSource.getLabel());
		final Filter filter =
			CachingHistoryUtils.UTILS.getFilterForLabel(label);
		final String serialized =
			CachingHistoryUtils.UTILS.serializeListFilter(filter);

		source.setHref("#" + serialized);
		source.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
					ListFilter listFilter =
						HistoryManager.HISTORY.getBaseListFilter();
					listFilter.setFilterObject(filter);
					HistoryManager.HISTORY
						.setListFilter(listFilter, serialized);
					event.preventDefault();
				}
			}
		});

		Link url = userSource.getSourceObject().getUrl();
		if (url != null && url.getValue() != null) {
			source.setTitle(url.getValue());
		} else if (SourceType.MANUAL.equals(userSource.getSourceType())) {
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

	private boolean contentHasBeenFilled = false;

	private void fillContent() {
		if (contentHasBeenFilled) {
			return;
		}
		contentHasBeenFilled = true;

		Item itemObject = getValue().getItemObject();
		ListingConstants constants = I18n.getListingConstants();

		if (itemObject.getContent() == null
			|| itemObject.getContent().getValue() == null) {
			content.addContent("NO",
				SafeHtmlUtils.fromString(constants.missingContent()));
			return;
		}

		if (itemObject instanceof PageChangeItem) {
			if (ContentType.HTML.equals(((PageChangeItem) itemObject)
				.getDiffType())) {
				Element div = DOM.createDiv();
				div.setInnerHTML(((PageChangeItem) itemObject)
					.getDiff()
					.getValue());
				content.addContent("DIFF_HTML", div);
			}
		}

		if (ContentType.HTML.equals((itemObject).getContentType())) {
			Element div = DOM.createDiv();
			div.setInnerHTML(itemObject.getContent().getValue());
			NodeList<Element> elementsByTagName = div.getElementsByTagName("a");
			for (int i = 0; i < elementsByTagName.getLength(); i++) {
				Element e = elementsByTagName.getItem(i);
				e.setAttribute("target", "_blank");
			}

			content.addContent("HTML", div);
		}
		{
			if (ContentType.PLAIN_TEXT.equals(itemObject.getContentType())) {
				String string = itemObject.getContent().getValue();
				content.addContent("TXT", string);
			} else {
				Element div = DOM.createDiv();
				div.setInnerHTML(itemObject.getContent().getValue());
				content.addContent("TXT", ClientTextContent.asPlainText(div));
			}
		}
	}

	@Override
	public void refresh() {
		labels.setNewData(getValue());
		setReadState();
		if (backupImage == null && getValue().getBackupBlobKey() != null) {
			backupImage = new Image(res.backup());
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

	@Override
	public void openOriginal() {
		Window.open(getValue().getItemObject().getUrl().getValue(), "_blank",
			"");
	}

	@Override
	public void openAddLabel() {
		labels.openSuggestion();
	}
}
