package cz.artique.client.i18n;

import com.google.gwt.core.client.GWT;

import cz.artique.client.ArtiqueConstants;
import cz.artique.client.hierarchy.tree.HierarchyTreeConstants;
import cz.artique.client.items.ItemsConstants;
import cz.artique.client.labels.LabelsConstants;
import cz.artique.client.listFilters.ListFiltersConstants;
import cz.artique.client.listing.ListingConstants;
import cz.artique.client.shortcuts.ShortcutsConstants;
import cz.artique.client.sources.SourcesConstants;

/**
 * Contains getters for all i18n constants classes.
 * 
 * @author Adam Juraszek
 * 
 */
public class I18n {
	private I18n() {}

	private static final SourcesConstants SOURCES_CONSTANTS = GWT
		.create(SourcesConstants.class);

	private static final ListFiltersConstants LIST_FILTERS_CONSTANTS = GWT
		.create(ListFiltersConstants.class);

	private static final LabelsConstants LABELS_CONSTANTS = GWT
		.create(LabelsConstants.class);

	private static final HierarchyTreeConstants HIERARCHY_TREE_CONSTANTS = GWT
		.create(HierarchyTreeConstants.class);

	private static final ArtiqueConstants ARTIQUE_CONSTANTS = GWT
		.create(ArtiqueConstants.class);

	private static final ShortcutsConstants SHORTCUTS_CONSTANTS = GWT
		.create(ShortcutsConstants.class);

	private static final ListingConstants LISTING_CONSTANTS = GWT
		.create(ListingConstants.class);

	private static final ItemsConstants ITEMS_CONSTANTS = GWT
		.create(ItemsConstants.class);

	/**
	 * Gets the sources constants.
	 * 
	 * @return the sources constants
	 */
	public static SourcesConstants getSourcesConstants() {
		return SOURCES_CONSTANTS;
	}

	/**
	 * Gets the list filters constants.
	 * 
	 * @return the list filters constants
	 */
	public static ListFiltersConstants getListFiltersConstants() {
		return LIST_FILTERS_CONSTANTS;
	}

	/**
	 * Gets the labels constants.
	 * 
	 * @return the labels constants
	 */
	public static LabelsConstants getLabelsConstants() {
		return LABELS_CONSTANTS;
	}

	/**
	 * Gets the hierarchy tree constants.
	 * 
	 * @return the hierarchy tree constants
	 */
	public static HierarchyTreeConstants getHierarchyTreeConstants() {
		return HIERARCHY_TREE_CONSTANTS;
	}

	/**
	 * Gets the Artique constants.
	 * 
	 * @return the Artique constants
	 */
	public static ArtiqueConstants getArtiqueConstants() {
		return ARTIQUE_CONSTANTS;
	}

	/**
	 * Gets the shortcuts constants.
	 * 
	 * @return the shortcuts constants
	 */
	public static ShortcutsConstants getShortcutsConstants() {
		return SHORTCUTS_CONSTANTS;
	}

	/**
	 * Gets the listing constants.
	 * 
	 * @return the listing constants
	 */
	public static ListingConstants getListingConstants() {
		return LISTING_CONSTANTS;
	}

	/**
	 * Gets the items constants.
	 * 
	 * @return the items constants
	 */
	public static ItemsConstants getItemsConstants() {
		return ITEMS_CONSTANTS;
	}
}
