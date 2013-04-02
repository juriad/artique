package cz.artique.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MessageBubbleWidget extends Composite {

	private static MessageBubbleUiBinder uiBinder = GWT
		.create(MessageBubbleUiBinder.class);
	private final Message message;

	@UiField
	Label closeButton;

	@UiField
	Label messageLabel;

	@UiField
	HorizontalPanel panel;

	interface MessageBubbleUiBinder
			extends UiBinder<Widget, MessageBubbleWidget> {}

	public MessageBubbleWidget(Message message) {
		this.message = message;
		initWidget(uiBinder.createAndBindUi(this));

		messageLabel.setText(message.getMessageBody());
		setStylePrimaryName("message");
		setStyleDependentName(message.getMessageType().name(), true);

		new Timer() {
			@Override
			public void run() {
				MessageBubbleWidget.this.removeFromParent();
			}
		}.schedule(message.getMessageType().getTimeout());
	}

	@UiHandler(value = "closeButton")
	protected void closeClicked(ClickEvent e) {
		this.removeFromParent();
	}

	public Message getMessage() {
		return message;
	}

}
