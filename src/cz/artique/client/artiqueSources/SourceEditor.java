package cz.artique.client.artiqueSources;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.i18n.ArtiqueMessages;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.HasRegion;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public class SourceEditor extends Composite
		implements HasEnabled, HasValue<Source> {

	private static SourceEditorUiBinder uiBinder = GWT
		.create(SourceEditorUiBinder.class);

	interface SourceEditorUiBinder extends UiBinder<Widget, SourceEditor> {}

	@UiField
	Grid grid;

	@UiField
	TextBox url;

	@UiField
	SourceTypePicker sourceType;

	@UiField
	SourceRegionPicker region;

	@UiField
	Button urlButton;

	private boolean enabled = true;

	private Source source;

	private Source parent;

	private AsyncCallback<Source> ping;

	public SourceEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		sourceType.setEnabled(enabled && source == null);
		region.setEnabled(enabled && source != null);
		urlButton.setEnabled(enabled && source == null);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Source> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void notPassedValidation() {
		urlButton.setEnabled(true);
		url.setEnabled(true);
		sourceType.setEnabled(true);
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
			constants.sourceCreatedError()));
	}

	private void passedValidation() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.sourceCreated()));
	}

	private void selectRegion() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.selectRegion()));
	}

	@UiHandler("urlButton")
	protected void deleteButtonClicked(ClickEvent event) {
		if (source != null) {
			urlButton.setEnabled(false);
			return;
		}
		if (url.getValue().trim().isEmpty()) {
			ArtiqueMessages messages = ArtiqueI18n.I18N.getMessages();
			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
			Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
				messages.errorEmptyField(constants.url())));
			// ignore
			return;
		}

		switch (sourceType.getValue()) {
		case RSS_ATOM:
			urlButton.setEnabled(false);
			url.setEnabled(false);
			sourceType.setEnabled(false);
			XMLSource s1 = new XMLSource(new Link(url.getValue()));
			Managers.SOURCES_MANAGER.createSource(s1,
				new AsyncCallback<XMLSource>() {
					public void onFailure(Throwable caught) {
						notPassedValidation();
					}

					public void onSuccess(XMLSource result) {
						passedValidation();
						source = result;
						if (ping != null) {
							ping.onSuccess(source);
						}
					}
				});
			break;
		case PAGE_CHANGE:
		case WEB_SITE:
			urlButton.setEnabled(false);
			url.setEnabled(false);
			sourceType.setEnabled(false);
			HTMLSource s2 = new HTMLSource(new Link(url.getValue()));
			Managers.SOURCES_MANAGER.createSource(s2,
				new AsyncCallback<HTMLSource>() {
					public void onFailure(Throwable caught) {
						notPassedValidation();
					}

					public void onSuccess(HTMLSource result) {
						parent = result;
						Element element = grid.getRowFormatter().getElement(2);
						element.getStyle().setVisibility(Visibility.VISIBLE);

						HasRegion hasRegion;

						if (SourceType.PAGE_CHANGE.equals(sourceType.getValue())) {
							PageChangeSource pageChangeSource =
								new PageChangeSource(parent.getUrl(), parent
									.getKey(), null);
							source = pageChangeSource;
							hasRegion = pageChangeSource;
						} else {
							WebSiteSource webSiteSource =
								new WebSiteSource(parent.getUrl(), parent
									.getKey(), null);
							source = webSiteSource;
							hasRegion = webSiteSource;
						}
						region.setValue(hasRegion);

						Managers.SOURCES_MANAGER.createSource(source,
							new AsyncCallback<Source>() {

								public void onFailure(Throwable caught) {
									notPassedValidation();
								}

								public void onSuccess(Source result) {
									region.setEnabled(true);
									ping.onSuccess(result);
									passedValidation();
									selectRegion();
								}
							});
					}
				});
			break;
		case MANUAL:
		default:
			// ignore
			return;
		}
	}

	public void saveSource() {
		region.saveRegion();
	}

	public Source getValue() {
		return source;
	}

	public void setValue(Source value) {
		source = value;
		parent = value != null ? value.getParentObject() : null;
		SourceType type =
			value != null
				? SourceType.get(value.getClass())
				: SourceType.RSS_ATOM;
		sourceType.setValue(type);
		sourceType.setEnabled(source == null);

		url.setValue(value == null ? "" : (value.getUrl() != null ? value
			.getUrl()
			.getValue() : ""));
		url.setEnabled(source == null);
		urlButton.setVisible(source == null);
		urlButton.setEnabled(source == null);

		if (source != null && source instanceof HasRegion) {
			region.setValue((HasRegion) source);
		} else {
			region.setValue(null);
		}

		Element element = grid.getRowFormatter().getElement(2);
		if (value == null) {
			element.getStyle().setVisibility(Visibility.HIDDEN);
		} else {
			if (type.getRegionType() == null) {
				element.getStyle().setDisplay(Display.NONE);
			} else {
				element.getStyle().clearDisplay();
				element.getStyle().clearVisibility();
			}
		}

		if (value != null)
			region.setEnabled(source != null);
	}

	public void setPing(AsyncCallback<Source> ping) {
		this.ping = ping;
	}

	public void setValue(Source value, boolean fireEvents) {
		setValue(value);
	}

}
