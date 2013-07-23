package cz.artique.client.labels.suggestion;

import cz.artique.shared.model.label.Label;

public class SuggestionResult {
	private final Label existingValue;
	private final String newValue;

	public SuggestionResult(Label existingValue) {
		this.existingValue = existingValue;
		newValue = null;
	}

	public SuggestionResult(String newValue) {
		existingValue = null;
		this.newValue = newValue;
	}

	public SuggestionResult() {
		existingValue = null;
		newValue = null;
	}

	public Label getExistingValue() {
		return existingValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public boolean isExisting() {
		return existingValue != null;
	}

	public boolean isNewValue() {
		return newValue != null;
	}

	public boolean hasValue() {
		return isExisting() || isNewValue();
	}
}
