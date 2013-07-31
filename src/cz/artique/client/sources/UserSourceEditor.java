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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

import cz.artique.client.common.ScrollableCellList;
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

/**
 * Editor shown inside {@link UserSourceDialog}; defines layout and control.
 * When the editor is supplied null value, the selection between custom and
 * recommended source is shown (this is hidden for existing source).
 * When the {@link Source} is created, the fields for {@link UserSource} are
 * shown.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserSourceEditor extends Composite implements HasValue<UserSource> {

	private static UserSourceEditorUiBinder uiBinder = GWT
		.create(UserSourceEditorUiBinder.class);

	interface UserSourceEditorUiBinder
			extends UiBinder<Widget, UserSourceEditor> {}

	/**
	 * Cell rendering recommended source.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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
	ScrollableCellList<Source> cellList;

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
	ToggleButton watching;

	@UiField
	TextBox hierarchy;

	@UiField
	SourceLabelsBar defaultLabels;

	@UiField
	InlineLabel lastCheck;

	@UiField
	InlineLabel errorSequence;

	@UiField
	PushButton nextCheck;

	@UiField
	SourceTypePicker sourceType;

	@UiField
	SourceRegionPicker region;

	private Boolean watchState;

	private Source source;

	private UserSource userSource;

	private Recommendation recommandation;

	private Button saveButton;

	private Button setUrlButton;

	/**
	 * Creates a new editor.
	 * Two buttons are injected inside.
	 * 
	 * @param setUrlButton
	 *            button clicked to create {@link Source}
	 * @param saveButton
	 *            button clicked to create or update {@link UserSource}
	 */
	public UserSourceEditor(Button setUrlButton, Button saveButton) {
		this.setUrlButton = setUrlButton;
		this.saveButton = saveButton;
		cellList = new ScrollableCellList<Source>(new SourceCell());
		initWidget(uiBinder.createAndBindUi(this));

		sourceSource
			.addValueChangeHandler(new ValueChangeHandler<SourceSource>() {
				public void onValueChange(ValueChangeEvent<SourceSource> event) {
					setSource();
				}
			});

		cellList.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				setFields();
			}
		});
	}

	/**
	 * Sets content of source-related fields when switching between recommended
	 * sources.
	 */
	protected void setFields() {
		Source selected = cellList.getSelected();
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

	/**
	 * Sets content of source-related fields when switching between custom and
	 * recommended source.
	 */
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

			cellList.clearSelection();
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
								if (result.getRecommendedSourcesObjects() == null) {
									result
										.setRecommendedSourcesObjects(new ArrayList<Source>());
								}
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

	/**
	 * Returns the {@link UserSource} described by the editor.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
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

	/**
	 * Shows select region message to user.
	 */
	private void selectRegion() {
		SourcesConstants constants = I18n.getSourcesConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.selectRegion()), true);
	}

	/**
	 * When user changed watching state.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("watching")
	protected void watchingChanged(ValueChangeEvent<Boolean> event) {
		if (watchState != null) {
			watchState = event.getValue();
			setWatch();
		}
	}

	/**
	 * Sets watching button to have the appropriate state.
	 */
	private void setWatch() {
		if (watchState == null
			|| SourceType.MANUAL.equals(sourceType.getValue())) {
			watching.setDown(false);
			watching.setEnabled(false);
		} else {
			watching.setEnabled(true);
			watching.setDown(watchState);
		}
	}

	/**
	 * Sets content of all fields in the editor according to the value.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue(UserSource value) {
		userSource = value;

		setUrlButton.setVisible(userSource.getKey() == null);
		setUrlButton.setEnabled(true);
		saveButton.setVisible(userSource.getKey() != null);

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

		// user source part

		name.setValue(userSource.getName());
		watchState =
			userSource.getKey() == null ? null : userSource.isWatching();
		setWatch();
		hierarchy.setValue(userSource.getHierarchy());

		defaultLabels.setNewData(userSource.getDefaultLabels());

		region.setValue(userSource);
		if (userSource.getKey() != null && type.isSupportRegion()
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
				nextCheck.getUpFace().setText(
					renderer
						.render(userSource.getSourceObject().getNextCheck()));
			} else {
				nextCheck.getUpFace().setText(constants.noCheckPlanned());
			}
			nextCheck.setEnabled(true);
		} else {
			lastCheck.setText(constants.unavailable());
			nextCheck.getUpFace().setText(constants.unavailable());
			errorSequence.setText(constants.unavailable());
			nextCheck.setEnabled(false);
		}

		for (int i = 4; i < grid.getRowCount(); i++) {
			Element element = grid.getRowFormatter().getElement(i);
			if (userSource.getKey() == null) {
				element.getStyle().setDisplay(Display.NONE);
			} else {
				element.getStyle().clearDisplay();
			}
		}

		// DANGER, constant number
		Element regionRow = grid.getRowFormatter().getElement(8);
		if (userSource.getSourceType() != null
			&& userSource.getSourceType().isSupportRegion()) {
			regionRow.getStyle().clearDisplay();
		} else {
			regionRow.getStyle().setDisplay(Display.NONE);
		}
	}

	/**
	 * Parses URL to get its domain part with schema.
	 * 
	 * @param url
	 *            URL to parse
	 * @return domain with schema
	 */
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

	/**
	 * Plan source check when user clicks on next check button.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("nextCheck")
	protected void checkNowClicked(ClickEvent event) {
		if (userSource.getKey() == null || source == null) {
			nextCheck.setEnabled(false);
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
					nextCheck.getUpFace().setText(
						renderer.render(userSource
							.getSourceObject()
							.getNextCheck()));
				}
			});
	}

	/**
	 * Create a new {@link Source} when user clicks on the Fix and continue
	 * button.
	 * Successful source creation causes call of {@link #sourceCreated(Source)}.
	 */
	public void setUrlButtonClicked() {
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
			Source selected = cellList.getSelected();
			if (selected != null) {
				newSource = selected;
				url = selected.getUrl().getValue();
			} else {
				return;
			}
		}

		final Element sourceRow = grid.getRowFormatter().getElement(0);
		sourceRow.getStyle().setDisplay(Display.NONE);

		setUrlButton.setEnabled(false);

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
						UserSourceEditor.this.setUrlButton.setEnabled(true);
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

	/**
	 * Sets the source created by clicking on Fix and continue.
	 * This makes the {@link UserSource} fields to be shown.
	 * 
	 * @param result
	 *            {@link Source}
	 */
	protected void sourceCreated(Source result) {
		source = result;

		UserSourceEditor.this.saveButton.setVisible(true);
		UserSourceEditor.this.setUrlButton.setVisible(false);

		for (int i = 4; i < grid.getRowCount(); i++) {
			Element e = grid.getRowFormatter().getElement(i);
			e.getStyle().clearDisplay();
		}

		Element element = grid.getRowFormatter().getElement(8);
		if (sourceType.getValue().isSupportRegion()) {
			UserSource us = new UserSource();
			us.setSourceObject(source);
			us.setSource(source.getKey());
			region.setValue(us);
			selectRegion();
			element.getStyle().clearDisplay();
		} else {
			element.getStyle().setDisplay(Display.NONE);
		}
	}
}
