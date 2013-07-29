package cz.artique.client.sources;

/**
 * Source of new source: either rcustom or recommended.
 * 
 * @author Adam Juraszek
 * 
 */
public enum SourceSource {
	/**
	 * Source added by direct input of URL by the user.
	 */
	CUSTOM,
	/**
	 * Source selected from list of recommended sources.
	 */
	RECOMMENDED;
}
