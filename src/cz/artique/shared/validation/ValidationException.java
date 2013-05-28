package cz.artique.shared.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	private final List<Issue<? extends HasIssue>> issues =
		new ArrayList<Issue<? extends HasIssue>>();

	public ValidationException(Issue<? extends HasIssue> issue) {
		getIssues().add(issue);
	}

	public List<Issue<? extends HasIssue>> getIssues() {
		return issues;
	}
}
