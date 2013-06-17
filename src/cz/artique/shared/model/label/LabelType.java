package cz.artique.shared.model.label;

import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;

/**
 * Defines types of {@link Label}s. User may only assign or remove labels of
 * type USER_DEFINED. Each source has its own USER_SOURCE label (
 * {@link UserSource#getLabel()}), this label is copied to all {@link UserItem}s
 * when they are created. SYSTEM labels are used only to define AND, OR
 * operators.
 * 
 * <p>
 * All types have defined shortcut which is used when {@link ListFilter} is
 * being serialized to URL.
 * 
 * @author Adam Juraszek
 * 
 */
public enum LabelType {
	/**
	 * All labels user create, assign and remove.
	 */
	USER_DEFINED("lusr"),
	/**
	 * Label of {@link UserSource} assigned to all UserItems belonging to that
	 * {@link UserSource}.
	 */
	USER_SOURCE("lsrc"),
	/**
	 * For internal usage only. {@link Label}s of operators AND, OR are of type
	 * SYSTEM.
	 */
	SYSTEM("lsys");

	private final String type;

	private LabelType(String type) {
		this.type = type;
	}

	/**
	 * Shorted than {@link #name()}.
	 * 
	 * @return shortcut of label type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets {@link LabelType} by shortcut.
	 * 
	 * @param type
	 *            shortcut
	 * @return labelType found by shortcut, null if not found
	 */
	public static LabelType getByType(String type) {
		for (LabelType lt : values()) {
			if (lt.getType().equals(type)) {
				return lt;
			}
		}
		return null;
	}
}
