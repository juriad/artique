package cz.artique.client.i18n;

import com.google.gwt.core.client.GWT;

public enum I18n {
	I18N;

	private final Constants constants = GWT
		.create(Constants.class);
	private final Messages messages = GWT.create(Messages.class);

	public Constants getConstants() {
		return constants;
	}

	public Messages getMessages() {
		return messages;
	}
}
