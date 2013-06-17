package cz.artique.shared.model.label;

/**
 * Level of filter. Either top level of second level.
 * 
 * @author Adam Juraszek
 * 
 */
public enum FilterLevel {
	/**
	 * Top level filter, combined by OR operator
	 */
	TOP_LEVEL_FILTER,
	/**
	 * Second level filter, combined by AND operator
	 */
	SECOND_LEVEL_FILTER,
}
