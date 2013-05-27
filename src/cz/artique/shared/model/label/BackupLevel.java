package cz.artique.shared.model.label;

public enum BackupLevel {
	NO_BACKUP(false),
	HTML(false),
	HTML_CSS(true);

	private final boolean inlineCss;

	private BackupLevel(boolean inlineCss) {
		this.inlineCss = inlineCss;

	}

	public boolean isInlineCss() {
		return inlineCss;
	}
}
