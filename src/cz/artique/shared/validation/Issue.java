package cz.artique.shared.validation;

import java.io.Serializable;

/**
 * Represents an issue of {@link IssueType} type related to a property of a
 * client request. Properties are defined in Client*Service interfaces.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            enum associated to service method which cased this issue
 */
public class Issue<E extends Enum<E> & HasIssue> implements Serializable {
	private static final long serialVersionUID = 1L;
	private E property;
	private IssueType issueType;

	@SuppressWarnings("unused")
	private Issue() {}

	/**
	 * @param property
	 * @param issueType
	 */
	public Issue(E property, IssueType issueType) {
		this.property = property;
		this.issueType = issueType;
	}

	/**
	 * @return property which caused this issue
	 */
	public E getProperty() {
		return property;
	}

	/**
	 * @return type of issue
	 */
	public IssueType getIssueType() {
		return issueType;
	}

	/**
	 * Used only during serialization.
	 * 
	 * @param property
	 *            the property which caused this issue
	 */
	public void setProperty(E property) {
		this.property = property;
	}

	/**
	 * Used only during serialization.
	 * 
	 * @param issueType
	 *            type of issue
	 */
	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

}
