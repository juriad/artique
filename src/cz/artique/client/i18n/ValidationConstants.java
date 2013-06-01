package cz.artique.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ValidationConstants extends ConstantsWithLookup {
	@DefaultStringValue("---")
	String GetClientConfigs_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetClientConfigs_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetClientConfigs_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetClientConfigs_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetClientConfigs_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get user configuration.")
	String GetClientConfigs_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got user configuration values.")
	String GetClientConfigs_GENERAL_OK();

	@DefaultStringValue("---")
	String SetClientConfigs_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String SetClientConfigs_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String SetClientConfigs_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String SetClientConfigs_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String SetClientConfigs_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to save user configuration; changes has been dropped.")
	String SetClientConfigs_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly saved changes of user configuration.")
	String SetClientConfigs_GENERAL_OK();

	@DefaultStringValue("---")
	String GetItems_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetItems_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetItems_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetItems_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetItems_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get items.")
	String GetItems_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got items.")
	String GetItems_GENERAL_OK();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_INVALID_VALUE();

	@DefaultStringValue("Failed to add null item.")
	String AddManualItem_USER_ITEM_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_USER_ITEM_OK();

	@DefaultStringValue("Type of the item must be manual.")
	String AddManualItem_ITEM_INVALID_VALUE();

	@DefaultStringValue("Failed to add null item.")
	String AddManualItem_ITEM_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_OK();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Cannot add label of a differend user.")
	String AddManualItem_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_LABELS_OK();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_CONTENT_OK();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TITLE_INVALID_VALUE();

	@DefaultStringValue("Title must not be empty.")
	String AddManualItem_ITEM_TITLE_EMPTY_OR_NULL();

	@DefaultStringValue("Title is too long. Maximum is 500 characters.")
	String AddManualItem_ITEM_TITLE_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TITLE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TITLE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TITLE_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_TITLE_OK();

	@DefaultStringValue("URL does not seem to be valid.")
	String AddManualItem_ITEM_URL_INVALID_VALUE();

	@DefaultStringValue("URL must be specified.")
	String AddManualItem_ITEM_URL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_URL_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_URL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_URL_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_URL_UNKNOWN();

	@DefaultStringValue("---")
	String AddManualItem_ITEM_URL_OK();

	@DefaultStringValue("---")
	String AddManualItem_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddManualItem_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddManualItem_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String AddManualItem_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddManualItem_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to add a new item manually.")
	String AddManualItem_GENERAL_UNKNOWN();

	@DefaultStringValue("A new item has been successfuly added.")
	String AddManualItem_GENERAL_OK();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Cannot add label of a differend user.")
	String UpdateItems_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateItems_LABELS_OK();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_INVALID_VALUE();

	@DefaultStringValue("Item must not be null.")
	String UpdateItems_ITEMS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateItems_ITEMS_OK();

	@DefaultStringValue("---")
	String UpdateItems_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateItems_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateItems_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateItems_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateItems_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to update items; local state is out-of-sync.")
	String UpdateItems_GENERAL_UNKNOWN();

	@DefaultStringValue("Items has been successfuly updated.")
	String UpdateItems_GENERAL_OK();

	@DefaultStringValue("---")
	String GetAllLabels_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetAllLabels_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetAllLabels_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetAllLabels_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetAllLabels_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get list of all your labels.")
	String GetAllLabels_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got list of all your labels.")
	String GetAllLabels_GENERAL_OK();

	@DefaultStringValue("---")
	String AddLabel_LABEL_INVALID_VALUE();

	@DefaultStringValue("Cannot add null label.")
	String AddLabel_LABEL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddLabel_LABEL_TOO_LONG();

	@DefaultStringValue("---")
	String AddLabel_LABEL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddLabel_LABEL_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddLabel_LABEL_UNKNOWN();

	@DefaultStringValue("---")
	String AddLabel_LABEL_OK();

	@DefaultStringValue("Label must not contail whitespaces and dollar sign.")
	String AddLabel_NAME_INVALID_VALUE();

	@DefaultStringValue("Label name must not be empty.")
	String AddLabel_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Label name is too long. Maximum is 500 characters.")
	String AddLabel_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String AddLabel_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddLabel_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddLabel_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String AddLabel_NAME_OK();

	@DefaultStringValue("---")
	String AddLabel_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddLabel_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddLabel_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String AddLabel_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddLabel_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to add a new label.")
	String AddLabel_GENERAL_UNKNOWN();

	@DefaultStringValue("A new label has been created.")
	String AddLabel_GENERAL_OK();

	@DefaultStringValue("---")
	String GetAllListFilters_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetAllListFilters_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetAllListFilters_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetAllListFilters_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetAllListFilters_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get list of all your filters.")
	String GetAllListFilters_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got list of all your filters.")
	String GetAllListFilters_GENERAL_OK();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_INVALID_VALUE();

	@DefaultStringValue("Cannot add null filter.")
	String AddListFilter_LIST_FILTER_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_UNKNOWN();

	@DefaultStringValue("---")
	String AddListFilter_LIST_FILTER_OK();

	@DefaultStringValue("---")
	String AddListFilter_NAME_INVALID_VALUE();

	@DefaultStringValue("Filter name must not be empty.")
	String AddListFilter_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Filter name is too long. Maximum is 500 characters.")
	String AddListFilter_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddListFilter_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddListFilter_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String AddListFilter_NAME_OK();

	@DefaultStringValue("---")
	String AddListFilter_HIERARCHY_INVALID_VALUE();

	@DefaultStringValue("Filter hierarchy must not be empty.")
	String AddListFilter_HIERARCHY_EMPTY_OR_NULL();

	@DefaultStringValue("Filter hierarchy is too long. Maximum is 500 characters.")
	String AddListFilter_HIERARCHY_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_HIERARCHY_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddListFilter_HIERARCHY_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddListFilter_HIERARCHY_UNKNOWN();

	@DefaultStringValue("---")
	String AddListFilter_HIERARCHY_OK();

	@DefaultStringValue("Export alias mustn't contain whitespaces.")
	String AddListFilter_EXPORT_ALIAS_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddListFilter_EXPORT_ALIAS_EMPTY_OR_NULL();

	@DefaultStringValue("Export alias is too long. Maximium is 500 characters.")
	String AddListFilter_EXPORT_ALIAS_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_EXPORT_ALIAS_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddListFilter_EXPORT_ALIAS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddListFilter_EXPORT_ALIAS_UNKNOWN();

	@DefaultStringValue("---")
	String AddListFilter_EXPORT_ALIAS_OK();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Filter cannot contain labels of a differend user.")
	String AddListFilter_FILTER_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String AddListFilter_FILTER_LABELS_OK();

	@DefaultStringValue("---")
	String AddListFilter_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddListFilter_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddListFilter_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String AddListFilter_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddListFilter_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to create a new list filter.")
	String AddListFilter_GENERAL_UNKNOWN();

	@DefaultStringValue("A new filter has been successfuly created.")
	String AddListFilter_GENERAL_OK();

	@DefaultStringValue("---")
	String UpdateListFilter_LIST_FILTER_INVALID_VALUE();

	@DefaultStringValue("Cannot update null filter.")
	String UpdateListFilter_LIST_FILTER_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateListFilter_LIST_FILTER_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_LIST_FILTER_ALREADY_EXISTS();

	@DefaultStringValue("Cannot update filter of a differend user.")
	String UpdateListFilter_LIST_FILTER_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateListFilter_LIST_FILTER_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateListFilter_LIST_FILTER_OK();

	@DefaultStringValue("---")
	String UpdateListFilter_NAME_INVALID_VALUE();

	@DefaultStringValue("Filter name must not be empty.")
	String UpdateListFilter_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Filter name is too long. Maximum is 500 characters.")
	String UpdateListFilter_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateListFilter_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateListFilter_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateListFilter_NAME_OK();

	@DefaultStringValue("---")
	String UpdateListFilter_HIERARCHY_INVALID_VALUE();

	@DefaultStringValue("Filter hierarchy must not be empty.")
	String UpdateListFilter_HIERARCHY_EMPTY_OR_NULL();

	@DefaultStringValue("Filter hierarchy is too long. Maximum is 500 characters.")
	String UpdateListFilter_HIERARCHY_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_HIERARCHY_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateListFilter_HIERARCHY_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateListFilter_HIERARCHY_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateListFilter_HIERARCHY_OK();

	@DefaultStringValue("Export alias mustn't contain whitespaces.")
	String UpdateListFilter_EXPORT_ALIAS_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateListFilter_EXPORT_ALIAS_EMPTY_OR_NULL();

	@DefaultStringValue("Export alias is too long. Maximium is 500 characters.")
	String UpdateListFilter_EXPORT_ALIAS_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_EXPORT_ALIAS_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateListFilter_EXPORT_ALIAS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateListFilter_EXPORT_ALIAS_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateListFilter_EXPORT_ALIAS_OK();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Filter cannot contain labels of a differend user.")
	String UpdateListFilter_FILTER_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateListFilter_FILTER_LABELS_OK();

	@DefaultStringValue("---")
	String UpdateListFilter_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateListFilter_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateListFilter_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateListFilter_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateListFilter_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to update filter.")
	String UpdateListFilter_GENERAL_UNKNOWN();

	@DefaultStringValue("Filter has been successfuly updated.")
	String UpdateListFilter_GENERAL_OK();

	@DefaultStringValue("---")
	String DeleteListFilter_LIST_FILTER_INVALID_VALUE();

	@DefaultStringValue("Cannot delete null filter.")
	String DeleteListFilter_LIST_FILTER_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String DeleteListFilter_LIST_FILTER_TOO_LONG();

	@DefaultStringValue("---")
	String DeleteListFilter_LIST_FILTER_ALREADY_EXISTS();

	@DefaultStringValue("Cannot delete filter of a differend user.")
	String DeleteListFilter_LIST_FILTER_SECURITY_BREACH();

	@DefaultStringValue("---")
	String DeleteListFilter_LIST_FILTER_UNKNOWN();

	@DefaultStringValue("---")
	String DeleteListFilter_LIST_FILTER_OK();

	@DefaultStringValue("---")
	String DeleteListFilter_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String DeleteListFilter_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String DeleteListFilter_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String DeleteListFilter_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String DeleteListFilter_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to delete filter.")
	String DeleteListFilter_GENERAL_UNKNOWN();

	@DefaultStringValue("Filter has been successfuly deleted.")
	String DeleteListFilter_GENERAL_OK();

	@DefaultStringValue("---")
	String AddSource_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Cannot check null source.")
	String AddSource_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddSource_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String AddSource_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddSource_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddSource_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String AddSource_SOURCE_OK();

	@DefaultStringValue("Source URL does not seem to be valid or reachable.")
	String AddSource_URL_INVALID_VALUE();

	@DefaultStringValue("Source URL must not be empty.")
	String AddSource_URL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddSource_URL_TOO_LONG();

	@DefaultStringValue("---")
	String AddSource_URL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddSource_URL_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddSource_URL_UNKNOWN();

	@DefaultStringValue("---")
	String AddSource_URL_OK();

	@DefaultStringValue("---")
	String AddSource_TYPE_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddSource_TYPE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddSource_TYPE_TOO_LONG();

	@DefaultStringValue("Manual source already exists.")
	String AddSource_TYPE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddSource_TYPE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddSource_TYPE_UNKNOWN();

	@DefaultStringValue("---")
	String AddSource_TYPE_OK();

	@DefaultStringValue("---")
	String AddSource_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddSource_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddSource_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String AddSource_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddSource_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Source did not passed validation.")
	String AddSource_GENERAL_UNKNOWN();

	@DefaultStringValue("Source has been verified.")
	String AddSource_GENERAL_OK();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Source for which regions are listed must not be null.")
	String GetRegions_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String GetRegions_SOURCE_OK();

	@DefaultStringValue("---")
	String GetRegions_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetRegions_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetRegions_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetRegions_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetRegions_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get list of regions.")
	String GetRegions_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got list of regions.")
	String GetRegions_GENERAL_OK();

	@DefaultStringValue("---")
	String AddUserSource_USER_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Cannot add null source.")
	String AddUserSource_USER_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_USER_SOURCE_TOO_LONG();

	@DefaultStringValue("Such source already exists. Watch the existing one.")
	String AddUserSource_USER_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_USER_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_USER_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_USER_SOURCE_OK();

	@DefaultStringValue("---")
	String AddUserSource_NAME_INVALID_VALUE();

	@DefaultStringValue("Source name must not be empty.")
	String AddUserSource_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Source name is too long. Maximum is 500 characters.")
	String AddUserSource_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_NAME_OK();

	@DefaultStringValue("Source type cannot be manual.")
	String AddUserSource_SOURCE_TYPE_INVALID_VALUE();

	@DefaultStringValue("Source type must be specified.")
	String AddUserSource_SOURCE_TYPE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TYPE_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TYPE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TYPE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TYPE_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TYPE_OK();

	@DefaultStringValue("---")
	String AddUserSource_HIERARCHY_INVALID_VALUE();

	@DefaultStringValue("Source hierarchy must not be empty.")
	String AddUserSource_HIERARCHY_EMPTY_OR_NULL();

	@DefaultStringValue("Source hierarchy is too long. Maximum is 500 characters.")
	String AddUserSource_HIERARCHY_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_HIERARCHY_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_HIERARCHY_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_HIERARCHY_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_HIERARCHY_OK();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Source must not be null.")
	String AddUserSource_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_SOURCE_OK();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Source cannot contain labels of a differend user.")
	String AddUserSource_DEFAULT_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_DEFAULT_LABELS_OK();

	@DefaultStringValue("The region is not ment to be used with this source.")
	String AddUserSource_REGION_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddUserSource_REGION_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_REGION_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_REGION_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_REGION_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_REGION_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_REGION_OK();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NAME_INVALID_VALUE();

	@DefaultStringValue("Region name must not be empty.")
	String AddUserSource_REGION_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Region name is too long. Maximum is 500 characters.")
	String AddUserSource_REGION_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NAME_OK();

	@DefaultStringValue("Positive selector is not a valid CSS selector.")
	String AddUserSource_REGION_POSITIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddUserSource_REGION_POSITIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Positive selector is too long. Maximum is 500 characters.")
	String AddUserSource_REGION_POSITIVE_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_REGION_POSITIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_REGION_POSITIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_REGION_POSITIVE_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_REGION_POSITIVE_OK();

	@DefaultStringValue("Negative selector is not a valid CSS selector.")
	String AddUserSource_REGION_NEGATIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NEGATIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Negative selector is too long. Maximum is 500 characters.")
	String AddUserSource_REGION_NEGATIVE_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NEGATIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NEGATIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NEGATIVE_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_REGION_NEGATIVE_OK();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_UNKNOWN();

	@DefaultStringValue("---")
	String AddUserSource_GENERAL_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_USER_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Cannot update null source.")
	String UpdateUserSource_USER_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_USER_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_USER_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("Cannot update source of a differend user.")
	String UpdateUserSource_USER_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_USER_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_USER_SOURCE_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_NAME_INVALID_VALUE();

	@DefaultStringValue("Source name must not be empty.")
	String UpdateUserSource_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Source name is too long. Maximum is 500 characters.")
	String UpdateUserSource_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_NAME_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_INVALID_VALUE();

	@DefaultStringValue("Source type must be specified.")
	String UpdateUserSource_SOURCE_TYPE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TYPE_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_HIERARCHY_INVALID_VALUE();

	@DefaultStringValue("Source hierarchy must not be empty.")
	String UpdateUserSource_HIERARCHY_EMPTY_OR_NULL();

	@DefaultStringValue("Source hierarchy is too long. Maximum is 500 characters.")
	String UpdateUserSource_HIERARCHY_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_HIERARCHY_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_HIERARCHY_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_HIERARCHY_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_HIERARCHY_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Source must be specified.")
	String UpdateUserSource_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_SOURCE_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_ALREADY_EXISTS();

	@DefaultStringValue("Source cannot contain labels of a differend user.")
	String UpdateUserSource_DEFAULT_LABELS_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_DEFAULT_LABELS_OK();

	@DefaultStringValue("The region is not ment to be used with this source.")
	String UpdateUserSource_REGION_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NAME_INVALID_VALUE();

	@DefaultStringValue("Region name must not be empty.")
	String UpdateUserSource_REGION_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Region name is too long. Maximum is 500 characters.")
	String UpdateUserSource_REGION_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NAME_OK();

	@DefaultStringValue("Positive selector is not a valid CSS selector.")
	String UpdateUserSource_REGION_POSITIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_POSITIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Positive selector is too long. Maximum is 500 characters.")
	String UpdateUserSource_REGION_POSITIVE_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_POSITIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_POSITIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_POSITIVE_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_POSITIVE_OK();

	@DefaultStringValue("Negative selector is not a valid CSS selector.")
	String UpdateUserSource_REGION_NEGATIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NEGATIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Negative selector is too long. Maximum is 500 characters.")
	String UpdateUserSource_REGION_NEGATIVE_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NEGATIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NEGATIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NEGATIVE_UNKNOWN();

	@DefaultStringValue("---")
	String UpdateUserSource_REGION_NEGATIVE_OK();

	@DefaultStringValue("---")
	String UpdateUserSource_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String UpdateUserSource_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String UpdateUserSource_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String UpdateUserSource_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String UpdateUserSource_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to update source.")
	String UpdateUserSource_GENERAL_UNKNOWN();

	@DefaultStringValue("Source has been successfuly updated.")
	String UpdateUserSource_GENERAL_OK();

	@DefaultStringValue("---")
	String GetUserSources_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String GetUserSources_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String GetUserSources_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String GetUserSources_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String GetUserSources_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to get list of all your sources.")
	String GetUserSources_GENERAL_UNKNOWN();

	@DefaultStringValue("Successfuly got list of all your sources.")
	String GetUserSources_GENERAL_OK();

	@DefaultStringValue("---")
	String CheckRegion_REGION_INVALID_VALUE();

	@DefaultStringValue("Cannot check null region.")
	String CheckRegion_REGION_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String CheckRegion_REGION_TOO_LONG();

	@DefaultStringValue("---")
	String CheckRegion_REGION_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String CheckRegion_REGION_SECURITY_BREACH();

	@DefaultStringValue("---")
	String CheckRegion_REGION_UNKNOWN();

	@DefaultStringValue("---")
	String CheckRegion_REGION_OK();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NAME_INVALID_VALUE();

	@DefaultStringValue("Region name must not be empty.")
	String CheckRegion_REGION_NAME_EMPTY_OR_NULL();

	@DefaultStringValue("Region name is too long. Maximum is 500 characters.")
	String CheckRegion_REGION_NAME_TOO_LONG();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NAME_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NAME_SECURITY_BREACH();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NAME_UNKNOWN();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NAME_OK();

	@DefaultStringValue("Positive selector is not a valid CSS selector.")
	String CheckRegion_REGION_POSITIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String CheckRegion_REGION_POSITIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Positive selector is too long. Maximum is 500 characters.")
	String CheckRegion_REGION_POSITIVE_TOO_LONG();

	@DefaultStringValue("---")
	String CheckRegion_REGION_POSITIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String CheckRegion_REGION_POSITIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String CheckRegion_REGION_POSITIVE_UNKNOWN();

	@DefaultStringValue("---")
	String CheckRegion_REGION_POSITIVE_OK();

	@DefaultStringValue("Negative selector is not a valid CSS selector.")
	String CheckRegion_REGION_NEGATIVE_INVALID_VALUE();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NEGATIVE_EMPTY_OR_NULL();

	@DefaultStringValue("Negative selector is too long. Maximum is 500 characters.")
	String CheckRegion_REGION_NEGATIVE_TOO_LONG();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NEGATIVE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NEGATIVE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NEGATIVE_UNKNOWN();

	@DefaultStringValue("---")
	String CheckRegion_REGION_NEGATIVE_OK();

	@DefaultStringValue("---")
	String CheckRegion_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String CheckRegion_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String CheckRegion_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String CheckRegion_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String CheckRegion_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to check region.")
	String CheckRegion_GENERAL_UNKNOWN();

	@DefaultStringValue("Region has been successfuly verified.")
	String CheckRegion_GENERAL_OK();

	@DefaultStringValue("Cannot plan check for manual source.")
	String PlanSourceCheck_SOURCE_INVALID_VALUE();

	@DefaultStringValue("Cannot plan check for non-existing source.")
	String PlanSourceCheck_SOURCE_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String PlanSourceCheck_SOURCE_TOO_LONG();

	@DefaultStringValue("---")
	String PlanSourceCheck_SOURCE_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String PlanSourceCheck_SOURCE_SECURITY_BREACH();

	@DefaultStringValue("---")
	String PlanSourceCheck_SOURCE_UNKNOWN();

	@DefaultStringValue("---")
	String PlanSourceCheck_SOURCE_OK();

	@DefaultStringValue("---")
	String PlanSourceCheck_GENERAL_INVALID_VALUE();

	@DefaultStringValue("---")
	String PlanSourceCheck_GENERAL_EMPTY_OR_NULL();

	@DefaultStringValue("---")
	String PlanSourceCheck_GENERAL_TOO_LONG();

	@DefaultStringValue("---")
	String PlanSourceCheck_GENERAL_ALREADY_EXISTS();

	@DefaultStringValue("---")
	String PlanSourceCheck_GENERAL_SECURITY_BREACH();

	@DefaultStringValue("Failed to plan check of source.")
	String PlanSourceCheck_GENERAL_UNKNOWN();

	@DefaultStringValue("Check has been planned; it will be performed withing a minute.")
	String PlanSourceCheck_GENERAL_OK();

}
