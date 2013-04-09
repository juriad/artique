package cz.artique.shared.model.label;

public enum LabelType {
	USER_DEFINED("lusr"),
	USER_SOURCE("lsrc"),
	SYSTEM("lsys");

	private final String type;

	LabelType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static LabelType getByType(String type) {
		for (LabelType lt : values()) {
			if (lt.getType().equals(type)) {
				return lt;
			}
		}
		return null;
	}
}
