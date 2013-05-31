package cz.artique.shared.validation;

import java.io.Serializable;

public class Issue<E extends Enum<E> & HasIssue> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final E property;
	private final IssueType issueType;

	public Issue(E property, IssueType issueType) {
		this.property = property;
		this.issueType = issueType;
	}

	public E getProperty() {
		return property;
	}

	public IssueType getIssueType() {
		return issueType;
	}
}
