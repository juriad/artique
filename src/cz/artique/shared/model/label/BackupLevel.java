package cz.artique.shared.model.label;

/**
 * Defines list of levels of backup.
 * 
 * @author Adam Juraszek
 * 
 */
public enum BackupLevel {
	/**
	 * No backup at all. This one is default because each backup is very
	 * expensive.
	 */
	NO_BACKUP(false),
	/**
	 * Backup only HTML; it also points all relative URLs in document to
	 * absolute ones.
	 */
	HTML(false),
	/**
	 * The same as {@link #HTML}, but it also fetches all stylesheets
	 * and puts their content to element style to head.
	 */
	HTML_CSS(true);

	private final boolean inlineCss;

	private BackupLevel(boolean inlineCss) {
		this.inlineCss = inlineCss;
	}

	/**
	 * Currently only {@link #HTML_CSS} returns true.
	 * 
	 * @return true if inlining CSS shall be performed, false otherwise
	 */
	public boolean isInlineCss() {
		return inlineCss;
	}
}
