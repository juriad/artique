package cz.artique.shared.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown on server side when validation fails.
 * Exception cannot be generic therefore list of issues is defined via
 * wildcards.
 * 
 * @author Adam Juraszek
 * 
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	private List<Issue<? extends HasIssue>> issues =
		new ArrayList<Issue<? extends HasIssue>>();

	@SuppressWarnings("unused")
	private ValidationException() {}

	/**
	 * Creates a new validation exception and adds initial issue.
	 * 
	 * @param issue
	 */
	public ValidationException(Issue<? extends HasIssue> issue) {
		getIssues().add(issue);
	}

	/**
	 * @return list of issues which caused this exception
	 */
	public List<Issue<? extends HasIssue>> getIssues() {
		return issues;
	}

	/**
	 * Used only during serialization.
	 * 
	 * @param issues
	 *            list of issues which caused this exception
	 */
	public void setIssues(List<Issue<? extends HasIssue>> issues) {
		this.issues = issues;
	}
}
