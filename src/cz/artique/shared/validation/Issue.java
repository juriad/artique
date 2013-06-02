package cz.artique.shared.validation;

import java.io.Serializable;

public class Issue<E extends Enum<E> & HasIssue> implements Serializable {
	private static final long serialVersionUID = 1L;
	private E property;
	private IssueType issueType;

	public Issue() {}

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

	public void setProperty(E property) {
		this.property = property;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

}
