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

public class ValidationMessage<E extends Enum<E> & HasIssue> {

	private final E general;

	public ValidationMessage(E general) {
		this.general = general;
	}

	private static final ValidationConstants constants = GWT
		.create(ValidationConstants.class);

	public void onFailure(Throwable caught) {
		ValidationException exception =
			new ValidationException(new Issue<E>(general, IssueType.UNKNOWN));
		onFailure(exception);
	}

	public void onFailure(ValidationException exception) {
		List<Issue<? extends HasIssue>> issues = exception.getIssues();
		for (Issue<? extends HasIssue> _issue : issues) {
			@SuppressWarnings("unchecked")
			Issue<E> issue = (Issue<E>) _issue;
			String enumName = issue.getProperty().enumName();
			String property = issue.getProperty().name();
			String issueType = issue.getIssueType().name();

			String method = enumName + "_" + property + "_" + issueType;
			Message message;
			try {
				String messageContent = constants.getString(method);
				message = new Message(MessageType.ERROR, messageContent);
			} catch (MissingResourceException e) {
				message =
					new Message(MessageType.SYSTEM,
						"Couldn't find validation resource: " + method);
			}
			Managers.MESSAGES_MANAGER.addMessage(message, true);
		}
	}

	public void onSuccess() {
		String enumName = general.enumName();
		String property = general.name();
		String method = enumName + "_" + property + "_" + "OK";
		Message message;
		try {
			String messageContent = constants.getString(method);
			message = new Message(MessageType.INFO, messageContent);
		} catch (MissingResourceException e) {
			message =
				new Message(MessageType.SYSTEM,
					"Couldn't find validation resource: " + method);
		}
		Managers.MESSAGES_MANAGER.addMessage(message, true);
	}
}
