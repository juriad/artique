package cz.artique.client.artiqueSources;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueLabels.ArtiqueLabelsBar;
import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.source.UserSource;

public class UserSourceEditor extends Composite
		implements HasEnabled, HasValue<UserSource> {

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
	TextBox hierarchy;

	@UiField
	ArtiqueLabelsBar defaultLabels;

	@UiField
	InlineLabel lastCheck;

	@UiField
	InlineLabel errorSequence;

	@UiField
	InlineLabel nextCheck;

	@UiField
	Button checkNow;

	private boolean enabled = true;

	private Key userSourceKey;

	private Key sourceKey;

	public UserSourceEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		name.setEnabled(enabled);
		hierarchy.setEnabled(enabled);
		defaultLabels.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public UserSource getValue() {
		UserSource us = new UserSource();
		us.setKey(userSourceKey);
		us.setUser(ArtiqueWorld.WORLD.getUser());
		us.setName(name.getValue());
		us.setHierarchy(hierarchy.getValue());

		if (sourceKey != null) {
			us.setSource(sourceKey);
		} else {
			// TODO new source
		}

		return us;
	}

	public void setValue(UserSource value) {
		userSourceKey = value.getKey();
		sourceKey = value.getSource();
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		name.setValue(value.getName());
		if (value.getKey() == null) {
			watching.setText(constants.watchingNotYet());
		} else if (value.isWatching()) {
			watching.setText(constants.watchingYes());
		} else {
			watching.setText(constants.watchingNo());
		}
		hierarchy.setValue(value.getHierarchy());

		// TODO defaultLabels

		if (value.getKey() != null && value.isWatching()) {
			DateTimeFormatRenderer renderer =
				new DateTimeFormatRenderer(
					DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM));
			if (value.getSourceObject().getLastCheck() != null) {
				lastCheck.setText(renderer.render(value
					.getSourceObject()
					.getLastCheck()));
			} else {
				lastCheck.setText(constants.notCheckedYet());
			}

			if (value.getSourceObject().getErrorSequence() > 0) {
				errorSequence.setText(value
					.getSourceObject()
					.getErrorSequence() + "");
			} else {
				errorSequence.setText(constants.noErrorSequence());
			}

			if (value.getSourceObject().getNextCheck() != null) {
				nextCheck.setText(renderer.render(value
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
			checkNow.setVisible(true);
		}
	}

	public void setValue(UserSource value, boolean fireEvents) {
		setValue(value);
	}

}
