package cz.artique.client.messages;

import java.util.List;
import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;

import cz.artique.client.i18n.ValidationConstants;
import cz.artique.client.manager.Managers;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

/**
 * Factory of {@link Message}s shown on success of failure.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of service method which caused this {@link ValidationMessage}
 */
public class ValidationMessage<E extends Enum<E> & HasIssue> {

	private final E general;

	public ValidationMessage(E general) {
		this.general = general;
	}

	private static final ValidationConstants constants = GWT
		.create(ValidationConstants.class);

	/**
	 * Shows message of failure of unknown cause with severity ERROR.
	 * 
	 * @param caught
	 *            exception
	 */
	public void onFailure(Throwable caught) {
		onFailure(caught, MessageType.ERROR);
	}

	/**
	 * Shows message of failure of unknown cause with adjustable severity.
	 * 
	 * @param caught
	 *            exception
	 */
	public void onFailure(Throwable caught, MessageType type) {
		ValidationException exception;

		if (caught instanceof ValidationException) {
			exception = (ValidationException) caught;
		} else {
			exception =
				new ValidationException(
					new Issue<E>(general, IssueType.UNKNOWN));
		}
		onFailure(exception, type);
	}

	/**
	 * Shows message of failed validation with severity ERROR.
	 * 
	 * @param exception
	 *            exception describing failed validation
	 */
	public void onFailure(ValidationException exception) {
		onFailure(exception, MessageType.ERROR);
	}

	/**
	 * Shows message of failed validation with adjustable severity.
	 * 
	 * @param exception
	 *            exception describing failed validation
	 */
	public void onFailure(ValidationException exception, MessageType type) {
		List<Issue<? extends HasIssue>> issues = exception.getIssues();
		for (Issue<? extends HasIssue> _issue : issues) {
			@SuppressWarnings("unchecked")
			Issue<E> issue = (Issue<E>) _issue;
			String enumName = issue.getProperty().enumName();
			String property = issue.getProperty().name();
			String issueType = issue.getIssueType().name();

			String method = enumName + "_" + property + "_" + issueType;
			showMessage(method, type);
		}
	}

	/**
	 * Shows message informing about success with severity INFO.
	 */
	public void onSuccess() {
		onSuccess(MessageType.INFO);
	}

	/**
	 * Shows message informing about success with adjustable severity.
	 */
	public void onSuccess(MessageType type) {
		String enumName = general.enumName();
		String property = general.name();
		String method = enumName + "_" + property + "_" + "OK";
		showMessage(method, type);
	}

	/**
	 * Shows message described by service method (either real or fake) which
	 * caused this message.
	 * 
	 * @param method
	 *            method of service
	 * @param type
	 *            severity
	 */
	private void showMessage(String method, MessageType type) {
		Message message;
		try {
			String messageContent = constants.getString(method);
			message = new Message(type, messageContent);
		} catch (MissingResourceException e) {
			message =
				new Message(MessageType.OFFLINE,
					"Couldn't find validation resource: " + method);
		}
		Managers.MESSAGES_MANAGER.addMessage(message, true);
	}
}
