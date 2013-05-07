package cz.artique.client.artiqueSources;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.shared.model.source.Source;
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

	private boolean enabled = true;

	private Source source;

	public SourceEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		sourceType.setEnabled(enabled);
		region.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Source> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Source getValue() {
		if (source != null) {
			return source;
		}
		SourceType type = sourceType.getValue();
		switch (type) {
		case MANUAL:
			return null;
		case RSS_ATOM:
			XMLSource s1 = new XMLSource(new Link(url.getValue()));
			return s1;
		case PAGE_CHANGE:
			break;
		case WEB_SITE:
			break;
		default:
			return null;
		}
		
		// TODO page change and web site
		return null;
	}

	public void setValue(Source value) {
		source = value;
		SourceType type =
			value != null
				? SourceType.get(value.getClass())
				: SourceType.RSS_ATOM;
		sourceType.setValue(type);
		region.setVisible(type.getRegionType() != null);
		setEnabled(value != null);
	}

	public void setValue(Source value, boolean fireEvents) {
		setValue(value);
	}

}
