package cz.artique.client.sources;

import java.util.Date;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.i18n.Constants;
import cz.artique.client.i18n.I18n;
import cz.artique.client.i18n.Messages;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
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

	@UiField
	Grid grid;

	@UiField
	TextBox name;

	@UiField
	InlineLabel watching;

	@UiField
	Button watchButton;

	@UiField
	TextBox hierarchy;

	// @UiField
	// ArtiqueLabelsBar defaultLabels;

	@UiField
	InlineLabel lastCheck;

	@UiField
	InlineLabel errorSequence;

	@UiField
	InlineLabel nextCheck;

	@UiField
	Button checkNow;

	@UiField
	TextBox url;

	@UiField
	SourceTypePicker sourceType;

	@UiField
	SourceRegionPicker region;

	@UiField
	Button urlButton;

	private Boolean watchState;

	private Source source;

	private UserSource userSource;

	public UserSourceEditor() {
		initWidget(uiBinder.createAndBindUi(this));
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

		// TODO default labels

		UserSource regUs = region.getValue();
		if (regUs.getRegionObject() != null) {
			us.setRegionObject(regUs.getRegionObject());
			us.setRegion(regUs.getRegion());
		}

		return us;
	}

	private void notPassedValidation() {
		urlButton.setEnabled(true);
		url.setEnabled(true);
		sourceType.setEnabled(true);
		Constants constants = I18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
			constants.sourceCreatedError()));
	}

	private void passedValidation() {
		Constants constants = I18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.sourceCreated()));
	}

	private void selectRegion() {
		Constants constants = I18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.selectRegion()));
	}

	@UiHandler("watchButton")
	protected void watchButtonClicked(ClickEvent event) {
		if (watchState != null) {
			watchState = !watchState;
			setWatch();
		}
	}

	private void setWatch() {
		Constants constants = I18n.I18N.getConstants();
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
		Constants constants = I18n.I18N.getConstants();

		// source part

		SourceType type =
			userSource.getSourceType() != null
				? userSource.getSourceType()
				: SourceType.RSS_ATOM;
		sourceType.setValue(type);
		sourceType.setEnabled(source == null);

		url.setValue(source == null ? "" : (source.getUrl() != null ? source
			.getUrl()
			.getValue() : ""));
		if (SourceType.MANUAL.equals(type)) {
			url.setValue(constants.unavailable());
		}
		url.setEnabled(source == null);
		urlButton.setVisible(source == null);
		urlButton.setEnabled(source == null);

		// user source part

		name.setValue(userSource.getName());
		watchState =
			userSource.getKey() == null ? null : userSource.isWatching();
		setWatch();
		hierarchy.setValue(userSource.getHierarchy());

		// TODO defaultLabels

		region.setValue(userSource);

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

		for (int i = 2; i < grid.getRowCount(); i++) {
			Element element = grid.getRowFormatter().getElement(i);
			if (userSource.getKey() == null) {
				element.getStyle().setDisplay(Display.NONE);
			} else {
				element.getStyle().clearDisplay();
			}
		}

		// DANGER, constant number
		Element regionRow = grid.getRowFormatter().getElement(6);
		if (userSource.getSourceType() != null
			&& userSource.getSourceType().getRegionType() != null) {
			regionRow.getStyle().clearDisplay();
		} else {
			regionRow.getStyle().setDisplay(Display.NONE);
		}
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
				public void onFailure(Throwable caught) {
					Messages messages = I18n.I18N.getMessages();
					Managers.MESSAGES_MANAGER.addMessage(new Message(
						MessageType.ERROR, messages.planCheckFailed(userSource
							.getName())));
				}

				public void onSuccess(Date result) {
					userSource.getSourceObject().setNextCheck(result);
					Messages messages = I18n.I18N.getMessages();
					Managers.MESSAGES_MANAGER.addMessage(new Message(
						MessageType.INFO, messages
							.sourceCheckPlanned(userSource.getName())));

					DateTimeFormatRenderer renderer =
						new DateTimeFormatRenderer(DateTimeFormat
							.getFormat(PredefinedFormat.DATE_TIME_MEDIUM));
					nextCheck.setText(renderer.render(userSource
						.getSourceObject()
						.getNextCheck()));
				}
			});
	}

	@UiHandler("urlButton")
	protected void urlButtonClicked(ClickEvent event) {
		if (source != null) {
			urlButton.setEnabled(false);
			return;
		}
		if (url.getValue().trim().isEmpty()) {
			Messages messages = I18n.I18N.getMessages();
			Constants constants = I18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.url())));
			// ignore
			return;
		}

		urlButton.setEnabled(false);
		url.setEnabled(false);
		sourceType.setEnabled(false);

		Source newSource = null;
		switch (sourceType.getValue()) {
		case RSS_ATOM:
			newSource = new XMLSource(new Link(url.getValue()));
			break;
		case PAGE_CHANGE:
			newSource = new PageChangeSource(new Link(url.getValue()));
			break;
		case WEB_SITE:
			newSource = new WebSiteSource(new Link(url.getValue()));
			break;
		case MANUAL:
		default:
			// ignore
			return;
		}
		Managers.SOURCES_MANAGER.createSource(newSource,
			new AsyncCallback<Source>() {
				public void onFailure(Throwable caught) {
					notPassedValidation();
				}

				public void onSuccess(Source result) {
					passedValidation();
					sourceCreated(result);
				}
			});
	}

	protected void sourceCreated(Source result) {
		source = result;
		for (int i = 2; i < grid.getRowCount(); i++) {
			Element e = grid.getRowFormatter().getElement(i);
			e.getStyle().clearDisplay();
		}

		Element element = grid.getRowFormatter().getElement(6);
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
