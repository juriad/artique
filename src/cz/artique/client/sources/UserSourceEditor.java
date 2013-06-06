package cz.artique.client.sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public class UserSourceEditor extends Composite implements HasValue<UserSource> {

	private static UserSourceEditorUiBinder uiBinder = GWT
		.create(UserSourceEditorUiBinder.class);

	interface UserSourceEditorUiBinder
			extends UiBinder<Widget, UserSourceEditor> {}

	class SourceCell extends AbstractCell<Source> {

		public SourceCell() {}

		@Override
		public void render(Context context, Source value, SafeHtmlBuilder sb) {
			// Value can be null, so do a null check..
			if (value == null) {
				return;
			}

			sb.appendHtmlConstant("<span class='sourceCell'>");
			sb.appendHtmlConstant("<span class='sourceType'>");
			sb.appendEscaped(sourceTypeAsString(value));
			sb.appendHtmlConstant("</span>");
			sb.appendEscaped(": ");
			sb.appendHtmlConstant("<span class='sourceUrl'>");
			sb.appendEscaped(getDomain(value.getUrl()));
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("</span>");
		}

		private String sourceTypeAsString(Source value) {
			SourceType type = SourceType.get(value.getClass());
			String method = "sourceType_" + type.name();
			String typeName = I18n.getSourcesConstants().getString(method);
			return typeName;
		}
	}

	@UiField(provided = true)
	CellList<Source> cellList;

	@UiField
	Grid grid;

	@UiField
	SourceSourcePicker sourceSource;

	@UiField
	TextBox name;

	@UiField
	Anchor urlAnchor;

	@UiField
	TextBox urlBox;

	@UiField
	Anchor domain;

	@UiField
	Button setUrlButton;

	@UiField
	InlineLabel watching;

	@UiField
	Button watchButton;

	@UiField
	TextBox hierarchy;

	@UiField
	SourceLabelsBar defaultLabels;

	@UiField
	InlineLabel lastCheck;

	@UiField
	InlineLabel errorSequence;

	@UiField
	InlineLabel nextCheck;

	@UiField
	Button checkNow;

	@UiField
	SourceTypePicker sourceType;

	@UiField
	SourceRegionPicker region;

	private Boolean watchState;

	private Source source;

	private UserSource userSource;

	private Recommendation recommandation;

	private final SingleSelectionModel<Source> selectionModel;

	public UserSourceEditor() {
		cellList = new CellList<Source>(new SourceCell());
		initWidget(uiBinder.createAndBindUi(this));

		sourceSource
			.addValueChangeHandler(new ValueChangeHandler<SourceSource>() {
				public void onValueChange(ValueChangeEvent<SourceSource> event) {
					setSource();
				}
			});

		selectionModel = new SingleSelectionModel<Source>();
		cellList.setSelectionModel(selectionModel);
		cellList.setStylePrimaryName("cellList");
		SourcesConstants constants = I18n.getSourcesConstants();
		cellList.setEmptyListWidget(new InlineLabel(constants
			.noRecommendation()));
		selectionModel
			.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					setFields();
				}
			});
	}

	protected void setFields() {
		Source selected = selectionModel.getSelectedObject();
		if (selected != null) {
			urlAnchor.setText(selected.getUrl().getValue());
			urlAnchor.setHref(selected.getUrl().getValue());
			String domainUrl = getDomain(selected.getUrl());
			domain.setText(domainUrl);
			domain.setHref(domainUrl);
			sourceType.setValue(SourceType.get(selected.getClass()));
			setUrlButton.setEnabled(true);
		} else {
			urlAnchor.setText(null);
			urlAnchor.setHref(null);
			domain.setText(null);
			domain.setHref(null);
			sourceType.setValue(SourceType.MANUAL);
			setUrlButton.setEnabled(false);
		}
	}

	private void setSource() {
		Element domainRow = grid.getRowFormatter().getElement(3);
		if (SourceSource.CUSTOM.equals(sourceSource.getValue())) {
			cellList.setVisible(false);
			sourceType.setEnabled(true);
			if (SourceType.MANUAL.equals(sourceType.getValue())) {
				sourceType.setValue(SourceType.RSS_ATOM);
			}

			urlBox.setValue(null);
			urlBox.setEnabled(true);
			urlBox.setVisible(true);
			urlAnchor.setText(null);
			urlAnchor.setHref(null);
			urlAnchor.setVisible(false);

			domainRow.getStyle().setDisplay(Display.NONE);
			setUrlButton.setEnabled(true);
		} else {
			cellList.setVisible(true);
			sourceType.setEnabled(false);
			sourceType.setValue(SourceType.MANUAL);

			urlBox.setEnabled(false);
			urlBox.setVisible(false);
			urlBox.setValue(null);
			urlAnchor.setVisible(true);
			urlAnchor.setText(null);
			urlAnchor.setHref(null);
			domainRow.getStyle().clearDisplay();
			domain.setText(null);
			setUrlButton.setEnabled(false);

			selectionModel.clear();
			if (recommandation != null) {
				cellList.setRowData(recommandation
					.getRecommendedSourcesObjects());
			} else {
				Managers.SOURCES_MANAGER
					.getRecommendation(new AsyncCallback<Recommendation>() {
						public void onFailure(Throwable caught) {
							recommandation = new Recommendation();
							recommandation
								.setRecommendedSourcesObjects(new ArrayList<Source>());
							cellList.setRowData(recommandation
								.getRecommendedSourcesObjects());
						}

						public void onSuccess(Recommendation result) {
							if (result == null) {
								recommandation = new Recommendation();
								recommandation
									.setRecommendedSourcesObjects(new ArrayList<Source>());
							} else {
								recommandation = result;
							}
							cellList.setRowData(recommandation
								.getRecommendedSourcesObjects());
						}
					});
			}
		}
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public UserSource getValue() {
		UserSource us = new UserSource();
		// empty name is handled in dialog

		if (sourceType.getValue() == null) {
			// fail silently
			return us;
		}

		if (source != null) {
			us.setSource(source.getKey());
			us.setSourceObject(source);
		} else {
			return us;
		}

		us.setKey(userSource.getKey());
		us.setUser(ArtiqueWorld.WORLD.getUser());
		us.setName(name.getValue());
		us.setHierarchy(hierarchy.getValue());
		us.setWatching(watchState == null ? true : watchState);
		us.setSourceType(sourceType.getValue());
		us.setCrawlerData(userSource.getCrawlerData());
		us.setLabel(userSource.getLabel());

		List<Label> allLabels = defaultLabels.getLabels();
		List<Key> labelKeys = new ArrayList<Key>();
		for (Label l : allLabels) {
			labelKeys.add(l.getKey());
		}
		// source label will be added to default labels on server
		us.setDefaultLabels(labelKeys);

		UserSource regUs = region.getValue();
		if (regUs.getRegionObject() != null) {
			us.setRegionObject(regUs.getRegionObject());
			us.setRegion(regUs.getRegion());
		}

		return us;
	}

	private void selectRegion() {
		SourcesConstants constants = I18n.getSourcesConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.selectRegion()), true);
	}

	@UiHandler("watchButton")
	protected void watchButtonClicked(ClickEvent event) {
		if (watchState != null) {
			watchState = !watchState;
			setWatch();
		}
	}

	private void setWatch() {
		SourcesConstants constants = I18n.getSourcesConstants();
		if (watchState == null
			|| SourceType.MANUAL.equals(sourceType.getValue())) {
			watching.setText(constants.unavailable());
			watchButton.setVisible(false);
		} else {
			watchButton.setVisible(true);
			if (watchState) {
				watching.setText(constants.watchingYes());
				watchButton.setText(constants.unwatchButton());
			} else {
				watching.setText(constants.watchingNo());
				watchButton.setText(constants.watchButton());
			}
		}
	}

	public void setValue(UserSource value) {
		userSource = value;
		source = userSource.getSourceObject();
		SourcesConstants constants = I18n.getSourcesConstants();

		// source part

		cellList.setVisible(false);
		Element sourceRow = grid.getRowFormatter().getElement(0);
		if (userSource.getKey() == null) {
			sourceSource.setValue(SourceSource.CUSTOM);
			sourceSource.setEnabled(true);
			recommandation = null;
			sourceRow.getStyle().clearDisplay();
		} else {
			sourceSource.setEnabled(false);
			sourceRow.getStyle().setDisplay(Display.NONE);
		}

		SourceType type =
			userSource.getSourceType() != null
				? userSource.getSourceType()
				: SourceType.RSS_ATOM;
		sourceType.setValue(type);
		sourceType.setEnabled(source == null);

		if (userSource.getKey() == null) {
			urlBox.setValue(null);
			urlBox.setEnabled(true);
			urlBox.setVisible(true);
			urlAnchor.setText(null);
			urlAnchor.setVisible(false);
		} else {
			urlBox.setEnabled(false);
			urlBox.setVisible(false);
			urlBox.setValue(null);
			urlAnchor.setVisible(true);
			String urlValue =
				source.getUrl() != null ? source.getUrl().getValue() : "";
			urlAnchor.setText(urlValue);
			urlAnchor.setHref(urlValue);
		}

		Element domainRow = grid.getRowFormatter().getElement(3);
		if (userSource.getKey() == null) {
			domainRow.getStyle().setDisplay(Display.NONE);
		} else {
			domainRow.getStyle().clearDisplay();
			String domainValue = getDomain(source.getUrl());
			domain.setText(domainValue);
			domain.setHref(domainValue);
		}

		Element setUrlRow = grid.getRowFormatter().getElement(4);
		if (userSource.getKey() == null) {
			setUrlButton.setEnabled(true);
			setUrlRow.getStyle().clearDisplay();
		} else {
			setUrlButton.setEnabled(false);
			setUrlRow.getStyle().setDisplay(Display.NONE);
		}
		// user source part

		name.setValue(userSource.getName());
		watchState =
			userSource.getKey() == null ? null : userSource.isWatching();
		setWatch();
		hierarchy.setValue(userSource.getHierarchy());

		UserSource us = new UserSource();
		us.setDefaultLabels(userSource.getDefaultLabels());
		defaultLabels.setNewData(us);

		region.setValue(userSource);
		if (userSource.getKey() != null && type.getRegionType() != null
			&& userSource.getRegion() == null) {
			selectRegion();
		}
		// stats

		if (userSource.getKey() != null && userSource.isWatching()
			&& !SourceType.MANUAL.equals(type)) {
			DateTimeFormatRenderer renderer =
				new DateTimeFormatRenderer(
					DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM));
			if (userSource.getSourceObject().getLastCheck() != null) {
				lastCheck.setText(renderer.render(userSource
					.getSourceObject()
					.getLastCheck()));
			} else {
				lastCheck.setText(constants.notCheckedYet());
			}

			if (userSource.getSourceObject().getErrorSequence() > 0) {
				errorSequence.setText(userSource
					.getSourceObject()
					.getErrorSequence() + "");
			} else {
				errorSequence.setText(constants.noErrorSequence());
			}

			if (userSource.getSourceObject().getNextCheck() != null) {
				nextCheck.setText(renderer.render(userSource
					.getSourceObject()
					.getNextCheck()));
			} else {
				nextCheck.setText(constants.noCheckPlanned());
			}
			checkNow.setVisible(true);
			checkNow.setEnabled(true);
		} else {
			lastCheck.setText(constants.unavailable());
			nextCheck.setText(constants.unavailable());
			errorSequence.setText(constants.unavailable());
			checkNow.setEnabled(false);
			checkNow.setVisible(false);
		}

		for (int i = 5; i < grid.getRowCount(); i++) {
			Element element = grid.getRowFormatter().getElement(i);
			if (userSource.getKey() == null) {
				element.getStyle().setDisplay(Display.NONE);
			} else {
				element.getStyle().clearDisplay();
			}
		}

		// DANGER, constant number
		Element regionRow = grid.getRowFormatter().getElement(9);
		if (userSource.getSourceType() != null
			&& userSource.getSourceType().getRegionType() != null) {
			regionRow.getStyle().clearDisplay();
		} else {
			regionRow.getStyle().setDisplay(Display.NONE);
		}
	}

	private String getDomain(Link url) {
		if (url == null) {
			return "";
		}
		String u = url.getValue();
		String u1 = u.replaceFirst(".*://", "");
		String u2 = u1.replaceFirst("/.*", "");
		String u3 = u.replace(u1, "");
		return u3 + u2;
	}

	public void setValue(UserSource value, boolean fireEvents) {
		setValue(value);
	}

	@UiHandler("checkNow")
	protected void checkNowClicked(ClickEvent event) {
		if (userSource.getKey() == null || source == null) {
			checkNow.setVisible(false);
			checkNow.setEnabled(false);
			return;
		}
		Managers.SOURCES_MANAGER.planSourceCheck(source.getKey(),
			new AsyncCallback<Date>() {
				public void onFailure(Throwable caught) {}

				public void onSuccess(Date result) {
					userSource.getSourceObject().setNextCheck(result);
					DateTimeFormatRenderer renderer =
						new DateTimeFormatRenderer(DateTimeFormat
							.getFormat(PredefinedFormat.DATE_TIME_MEDIUM));
					nextCheck.setText(renderer.render(userSource
						.getSourceObject()
						.getNextCheck()));
				}
			});
	}

	@UiHandler("setUrlButton")
	protected void setUrlButtonClicked(ClickEvent event) {
		if (source != null) {
			return;
		}
		if (SourceType.MANUAL.equals(sourceType.getValue())) {
			return;
		}

		final String url;
		Source newSource = null;
		if (sourceSource.getValue().equals(SourceSource.CUSTOM)) {
			sourceType.setEnabled(false);
			url = urlBox.getValue();
		} else {
			Source selected = selectionModel.getSelectedObject();
			if (selected != null) {
				newSource = selected;
				url = selected.getUrl().getValue();
			} else {
				return;
			}
		}

		final Element sourceRow = grid.getRowFormatter().getElement(0);
		sourceRow.getStyle().setDisplay(Display.NONE);

		final Element setUrlRow = grid.getRowFormatter().getElement(4);
		setUrlButton.setEnabled(false);
		setUrlRow.getStyle().setDisplay(Display.NONE);

		final Element domainRow = grid.getRowFormatter().getElement(3);
		domainRow.getStyle().clearDisplay();

		urlBox.setVisible(false);
		urlAnchor.setVisible(true);
		urlAnchor.setHref(url);
		urlAnchor.setText(url);
		String domainValue = getDomain(new Link(url));
		domain.setText(domainValue);
		domain.setHref(domainValue);

		if (newSource == null) {
			switch (sourceType.getValue()) {
			case RSS_ATOM:
				newSource = new XMLSource(new Link(url));
				break;
			case PAGE_CHANGE:
				newSource = new PageChangeSource(new Link(url));
				break;
			case WEB_SITE:
				newSource = new WebSiteSource(new Link(url));
				break;
			case MANUAL:
			default:
				// ignore
				return;
			}
			Managers.SOURCES_MANAGER.addSource(newSource,
				new AsyncCallback<Source>() {
					public void onFailure(Throwable caught) {
						setUrlButton.setEnabled(true);
						setUrlRow.getStyle().clearDisplay();
						sourceRow.getStyle().clearDisplay();

						sourceType.setEnabled(true);
						urlBox.setVisible(true);
						urlBox.setValue(url);
						urlAnchor.setVisible(false);

						if (SourceSource.CUSTOM.equals(sourceSource.getValue())) {
							domainRow.getStyle().setDisplay(Display.NONE);
						}
					}

					public void onSuccess(Source result) {
						sourceCreated(result);
					}
				});
		} else {
			sourceCreated(newSource);
		}
	}

	protected void sourceCreated(Source result) {
		source = result;
		for (int i = 5; i < grid.getRowCount(); i++) {
			Element e = grid.getRowFormatter().getElement(i);
			e.getStyle().clearDisplay();
		}

		Element element = grid.getRowFormatter().getElement(9);
		if (sourceType.getValue().getRegionType() != null) {
			UserSource us = new UserSource();
			us.setSourceObject(source);
			us.setSource(source.getKey());
			us.setUser(ArtiqueWorld.WORLD.getUser());
			region.setValue(us);
			selectRegion();
			element.getStyle().clearDisplay();
		} else {
			element.getStyle().setDisplay(Display.NONE);
		}
	}
}
