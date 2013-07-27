package cz.artique.client.labels.suggestion;

import cz.artique.shared.model.label.Label;

/**
 * Selection result of label suggestion.
 * 
 * @author Adam Juraszek
 * 
 */
public class SuggestionResult {
	private final Label existingValue;
	private final String newValue;

	/**
	 * When existing {@link Label} was selected.
	 * 
	 * @param existingValue
	 *            existing {@link Label}
	 */
	public SuggestionResult(Label existingValue) {
		this.existingValue = existingValue;
		newValue = null;
	}

	/**
	 * When potentially non-existent {@link Label} was selected.
	 * 
	 * @param newValue
	 *            name of {@link Label}
	 */
	public SuggestionResult(String newValue) {
		existingValue = null;
		this.newValue = newValue;
	}

	/**
	 * When no {@link Label} was selected; the selection was canceled.
	 */
	public SuggestionResult() {
		existingValue = null;
		newValue = null;
	}

	/**
	 * @return existing {@link Label}
	 */
	public Label getExistingValue() {
		return existingValue;
	}

	/**
	 * @return name of potentially non-existent {@link Label}
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @return whether the result is existing {@link Label}
	 */
	public boolean isExisting() {
		return existingValue != null;
	}

	/**
	 * @return whether the result is name of potentially non-existing
	 *         {@link Label}
	 */
	public boolean isNewValue() {
		return newValue != null;
	}

	/**
	 * @return whether a selection exists
	 */
	public boolean hasValue() {
		return isExisting() || isNewValue();
	}
}
