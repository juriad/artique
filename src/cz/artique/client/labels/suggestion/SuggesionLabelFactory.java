package cz.artique.client.labels.suggestion;

import com.google.gwt.user.client.ui.ValueLabel;

public interface SuggesionLabelFactory<E> {
	ValueLabel<E> createLabel();
}
