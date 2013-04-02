package cz.artique.client.messages;

import com.google.gwt.event.shared.GwtEvent;

public class MessageEvent extends GwtEvent<MessageHandler> {

	private static final Type<MessageHandler> TYPE = new Type<MessageHandler>();
	private final Message message;

	public static Type<MessageHandler> getType() {
		return TYPE;
	}

	public MessageEvent(Message message) {
		this.message = message;
	}

	@Override
	public final Type<MessageHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageHandler handler) {
		handler.onMessageAdded(this);
	}

	public Message getMessage() {
		return message;
	}

}
