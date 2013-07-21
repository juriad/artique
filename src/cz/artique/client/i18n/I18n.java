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

	public static SourcesConstants getSourcesConstants() {
		return SOURCES_CONSTANTS;
	}

	public static ListFiltersConstants getListFiltersConstants() {
		return LIST_FILTERS_CONSTANTS;
	}

	public static LabelsConstants getLabelsConstants() {
		return LABELS_CONSTANTS;
	}

	public static HierarchyTreeConstants getHierarchyTreeConstants() {
		return HIERARCHY_TREE_CONSTANTS;
	}

	public static ArtiqueConstants getArtiqueConstants() {
		return ARTIQUE_CONSTANTS;
	}

	public static ShortcutsConstants getShortcutsConstants() {
		return SHORTCUTS_CONSTANTS;
	}

	public static ListingConstants getListingConstants() {
		return LISTING_CONSTANTS;
	}

	public static ItemsConstants getItemsConstants() {
		return ITEMS_CONSTANTS;
	}
}
