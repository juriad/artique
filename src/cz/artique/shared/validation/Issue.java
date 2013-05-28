package cz.artique.shared.validation;

import java.io.Serializable;

public class Issue<E extends Enum<E> & HasIssue> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Enum<E> property;
	private final IssueType issueType;

	public Issue(Enum<E> property, IssueType issueType) {
		this.property = property;
		this.issueType = issueType;
	}

	public Enum<E> getProperty() {
		return property;
	}

	public IssueType getIssueType() {
		return issueType;
	}
}
