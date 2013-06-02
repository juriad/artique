package cz.artique.shared.model.label;

import java.io.Serializable;

public class LabelAppearance implements Serializable {

	private static final long serialVersionUID = 1L;

	private String foregroundColor;

	private String backgroundColor;

	public LabelAppearance() {}

	public LabelAppearance(LabelAppearance other) {
		if (other != null) {
			this.backgroundColor = other.getBackgroundColor();
			this.foregroundColor = other.getForegroundColor();
		}
	}

	public String getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
