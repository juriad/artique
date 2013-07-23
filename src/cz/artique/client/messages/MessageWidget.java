package cz.artique.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.common.CloseButton;

public class MessageWidget extends Composite {
	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("MessageWidget.css")
		CssResource style();

		@Source("../icons/dialog-error.png")
		ImageResource error();

		@Source("../icons/dialog-warning.png")
		ImageResource warning();

		@Source("../icons/dialog-information.png")
		ImageResource info();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private static MessageUiBinder uiBinder = GWT.create(MessageUiBinder.class);
	private final Message message;

	@UiField(provided = true)
	CloseButton<MessageWidget> closeButton;

	@UiField
	Label messageLabel;

	@UiField
	FlowPanel panel;

	interface MessageUiBinder extends UiBinder<Widget, MessageWidget> {}

	public MessageWidget(Message message) {
		res.style().ensureInjected();
		this.message = message;
		closeButton = new CloseButton<MessageWidget>(this);
		initWidget(uiBinder.createAndBindUi(this));

		messageLabel.setText(message.getMessageBody());
		messageLabel.setStylePrimaryName("messageContent");
		setStylePrimaryName("message");
		setStyleDependentName(message.getMessageType().name(), true);

		if (MessageType.FAILURE.equals(message.getMessageType())) {
			closeButton.setEnabled(false);
		}

		if (message.getMessageType().getTimeout() > 0) {
			new Timer() {
				@Override
				public void run() {
					MessageWidget.this.removeFromParent();
				}
			}.schedule(message.getMessageType().getTimeout());
		}
	}

	@UiHandler(value = "closeButton")
	protected void closeClosed(CloseEvent<MessageWidget> e) {
		this.removeFromParent();
	}

	public Message getMessage() {
		return message;
	}

}
