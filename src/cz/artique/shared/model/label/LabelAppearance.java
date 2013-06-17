package cz.artique.shared.model.label;

import java.io.Serializable;

/**
 * Appearance of label when shown. Controls foreground and background color.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelAppearance implements Serializable {

	private static final long serialVersionUID = 1L;

	private String foregroundColor;

	private String backgroundColor;

	/**
	 * Default constructor.
	 */
	public LabelAppearance() {}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            other label appearance
	 */
	public LabelAppearance(LabelAppearance other) {
		if (other != null) {
			this.backgroundColor = other.getBackgroundColor();
			this.foregroundColor = other.getForegroundColor();
		}
	}

	/**
	 * @return foreground color
	 */
	public String getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor
	 *            foreground color
	 */
	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * @return background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            background color
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
