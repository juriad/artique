package cz.artique.client.messages;

import cz.artique.shared.utils.HasHierarchy;

public class Message implements HasHierarchy {
	private MessageType messageType;

	private String messageBody;

	public Message(MessageType messageType, String messageBody) {
		super();
		this.messageType = messageType;
		this.messageBody = messageBody;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getHierarchy() {
		return "/";
	}

	public void setHierarchy(String hierarchy) {}

	public String getName() {
		return messageBody;
	}
}
