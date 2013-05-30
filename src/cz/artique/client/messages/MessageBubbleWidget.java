package cz.artique.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.common.CloseButton;

public class MessageBubbleWidget extends Composite {

	private static MessageBubbleUiBinder uiBinder = GWT
		.create(MessageBubbleUiBinder.class);
	private final Message message;

	@UiField(provided = true)
	CloseButton<MessageBubbleWidget> closeButton;

	@UiField
	Label messageLabel;

	@UiField
	FlowPanel panel;

	interface MessageBubbleUiBinder
			extends UiBinder<Widget, MessageBubbleWidget> {}

	public MessageBubbleWidget(Message message) {
		this.message = message;
		closeButton = new CloseButton<MessageBubbleWidget>(this);
		initWidget(uiBinder.createAndBindUi(this));

		messageLabel.setText(message.getMessageBody());
		messageLabel.setStylePrimaryName("messageContent");
		setStylePrimaryName("message");
		setStyleDependentName(message.getMessageType().name(), true);

		if (message.getMessageType().getTimeout() > 0) {
			new Timer() {
				@Override
				public void run() {
					MessageBubbleWidget.this.removeFromParent();
				}
			}.schedule(message.getMessageType().getTimeout());
		}
	}

	@UiHandler(value = "closeButton")
	protected void closeClosed(CloseEvent<MessageBubbleWidget> e) {
		this.removeFromParent();
	}

	public Message getMessage() {
		return message;
	}

}
