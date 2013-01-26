package cz.artique.client.labels.suggestion;

public class SuggestionResult<E> {
	private final E existingValue;
	private final String newValue;
	private final boolean existing;
	private final boolean hasValue;

	public SuggestionResult(E existingValue) {
		this.existingValue = existingValue;
		existing = true;
		newValue = null;
		hasValue = true;
	}

	public SuggestionResult(String newValue) {
		existingValue = null;
		existing = false;
		this.newValue = newValue;
		hasValue = true;
	}

	public SuggestionResult() {
		existingValue = null;
		existing = false;
		newValue = null;
		hasValue = false;
	}

	public E getExistingValue() {
		return existingValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public boolean isExisting() {
		return existing;
	}

	public boolean isHasValue() {
		return hasValue;
	}
}