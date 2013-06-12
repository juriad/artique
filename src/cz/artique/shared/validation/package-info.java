/**
 * Validation package contains all classes which are used both during
 * server-side validation and client-side validation message processing.
 * 
 * <p>
 * It consists of {@link cz.artique.shared.validation.ValidationException} class
 * which is thrown on server side if validation fails. Exception as list of
 * {@link cz.artique.shared.validation.Issue}s, which caused the exception. Each
 * Issue consists of enum field property which carries the information about
 * property which whose value is invalid and type of issue (
 * {@link cz.artique.shared.validation.IssueType}).
 * 
 * <p>
 * There are more validation-related classes in this application. Some are on
 * the server-side:
 * <ul>
 * <li>{@link cz.artique.server.validation.Validator} - Performs usual
 * validation.
 * </ul>
 * Others are on client-side:
 * <ul>
 * <li>{@link cz.artique.client.messages.ValidationMessage} - Shows appropriate
 * message for a validation exception.
 * <li>{@link cz.artique.client.i18n.ValidationConstants} - Set of localizable
 * constant messages which are shown by
 * {@link cz.artique.client.messages.ValidationMessage}.
 * <li>Client*Service interfaces define bunch of enums each providing list of
 * properties which could cause validation exception.
 * </ul>
 * 
 */
package cz.artique.shared.validation;

