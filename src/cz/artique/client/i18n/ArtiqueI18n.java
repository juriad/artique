package cz.artique.client.i18n;

import com.google.gwt.core.client.GWT;

public enum ArtiqueI18n {
	I18N;

	private final ArtiqueConstants constants = GWT
		.create(ArtiqueConstants.class);
	private final ArtiqueMessages messages = GWT.create(ArtiqueMessages.class);

	public ArtiqueConstants getConstants() {
		return constants;
	}

	public ArtiqueMessages getMessages() {
		return messages;
	}
}
