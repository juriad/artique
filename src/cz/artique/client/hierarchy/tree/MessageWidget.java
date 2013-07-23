package cz.artique.client.hierarchy.tree;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.i18n.I18n;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessagesManager;

public class MessageWidget extends AbstractHierarchyTreeWidget<Message> {

	public static class MessageWidgetFactory
			implements HierarchyTreeWidgetFactory<Message> {

		public static final HierarchyTreeWidgetFactory<Message> FACTORY =
			new MessageWidgetFactory();

		public HierarchyTreeWidget<Message> createWidget(
				Hierarchy<Message> hierarchy) {
			return new MessageWidget(hierarchy);
		}
	}

	private static ClickHandler clearHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			MessagesManager.MESSENGER.clear();
		}
	};

	public MessageWidget(Hierarchy<Message> hierarchy) {
		super(hierarchy);

		if (hierarchy instanceof LeafNode) {
			createLeafNodePanel(getPanel());
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel(getPanel());
			} else {
				createRootPanel(getPanel());
			}
		}
	}

	private void createRootPanel(FlowPanel panel) {
		String clearTooltip =
			I18n.getHierarchyTreeConstants().clearMessagesTooltip();
		createAnchor(panel, I18n.getHierarchyTreeConstants().messageRootText(),
			null, null, null);
		createImage(panel, ArtiqueWorld.WORLD.getResources().clear(),
			clearHandler, clearTooltip);
	}

	private void createInnerNodePanel(FlowPanel panel) {
		createLabel(panel, getHierarchy().getName(), null, null);
	}

	private void createLeafNodePanel(FlowPanel panel) {
		LeafNode<Message> leaf = (LeafNode<Message>) getHierarchy();
		final Message item = leaf.getItem();

		InlineLabel messageLabel =
			createLabel(panel, item.getMessageBody(), new ClickHandler() {
				public void onClick(ClickEvent event) {
					MessagesManager.MESSENGER.addMessage(item, false);
				}
			}, null);
		messageLabel.setStylePrimaryName("message");
		messageLabel.setStyleDependentName(item.getMessageType().name(), true);
	}

}
