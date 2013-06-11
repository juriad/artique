package cz.artique.shared.validation;

/**
 * This enum provides list of possible types of issues which may occur during
 * validation.
 * 
 * The name of IssueType is used by messaging system on client to identify
 * appropriate message to show.
 * 
 * @author Adam Juraszek
 * 
 */
public enum IssueType {
	/**
	 * Invalid value.
	 */
	INVALID_VALUE,

	/**
	 * The property is null or empty string.
	 */
	EMPTY_OR_NULL,

	/**
	 * The property content is too long; virtually all string properties are
	 * upper-bounded to 500 characters due to datastore limitation.
	 */
	TOO_LONG,

	/**
	 * Such object already exists, it cannot be created once more.
	 */
	ALREADY_EXISTS,

	/**
	 * Specified user does not equal effective user or user requested to
	 * manipulate an object which does not belong to him.
	 */
	SECURITY_BREACH,

	/**
	 * Never used during validation. Represents network problem or any exception
	 * thrown by server which is not validation-related.
	 */
	UNKNOWN,

	/**
	 * Never used during validation. No error occurred, everything is fine.
	 * This type is used by messaging system to unify all sucess and failure
	 * messages.
	 */
	OK;
}
