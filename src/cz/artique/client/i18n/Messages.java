package cz.artique.client.i18n;

public interface Messages extends com.google.gwt.i18n.client.Messages {
	@DefaultMessage("Filter {0} has been created.")
	String listFilterCreated(String name);

	@DefaultMessage("Filter {0} has been updated.")
	String listFilterUpdated(String name);

	@DefaultMessage("Filter {0} has been deleted.")
	String listFilterDeleted(String name);

	@DefaultMessage("Failed to create filter {0}.")
	String listFilterCreatedError(String name);

	@DefaultMessage("Filter to update filter {0}.")
	String listFilterUpdatedError(String name);

	@DefaultMessage("Failed to delete filter {0}.")
	String listFilterDeletedError(String name);

	@DefaultMessage("{0} must be specified.")
	String errorEmptyField(String field);

	@DefaultMessage("Source {0} has been created.")
	String userSourceCreated(String name);

	@DefaultMessage("Source {0} has been updated.")
	String userSourceUpdated(String name);

	@DefaultMessage("Failed to create source {0}.")
	String userSourceCreatedError(String name);

	@DefaultMessage("Filter to update source {0}.")
	String userSourceUpdatedError(String name);

	@DefaultMessage("Failed to plan check for source {0}.")
	String planCheckFailed(String name);

	@DefaultMessage("Check for source {0} has been planned;\n"
		+ "it will be performed withing a minute.")
	String sourceCheckPlanned(String name);

	@DefaultMessage("Manual item {0} has been created.")
	String manualItemCreated(String title);

	@DefaultMessage("Failed to create manual item {0}.")
	String errorCreatingManualItem(String title);
}
