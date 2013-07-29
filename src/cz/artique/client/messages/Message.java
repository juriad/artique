package cz.artique.client.messages;

import cz.artique.client.hierarchy.tree.AbstractHierarchyTree;
import cz.artique.shared.utils.HasHierarchy;

/**
 * Represents a message informing user about success or failure of an operation.
 * 
 * @author Adam Juraszek
 * 
 */
public class Message implements HasHierarchy {
	private MessageType messageType;

	private String messageBody;

	/**
	 * @param messageType
	 *            severity of message
	 * @param messageBody
	 *            text of message
	 */
	public Message(MessageType messageType, String messageBody) {
		super();
		this.messageType = messageType;
		this.messageBody = messageBody;
	}

	/**
	 * @return severity of message
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            severity of message
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return text of message
	 */
	public String getMessageBody() {
		return messageBody;
	}

	/**
	 * @param messageBody
	 *            text of message
	 */
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	/**
	 * Returns "/"; allows us to show messages inside
	 * {@link AbstractHierarchyTree}.
	 * 
	 * @see cz.artique.shared.utils.HasHierarchy#getHierarchy()
	 */
	public String getHierarchy() {
		return "/";
	}

	/**
	 * Returns text of message.
	 * 
	 * @see cz.artique.shared.utils.HasHierarchy#getName()
	 */
	public String getName() {
		return messageBody;
	}
}
