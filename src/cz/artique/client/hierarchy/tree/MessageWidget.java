package cz.artique.client.hierarchy.tree;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.i18n.I18n;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessagesManager;

/**
 * Widget representing {@link Message} in {@link MessagesTree}.
 * 
 * @author Adam Juraszek
 * 
 */
public class MessageWidget extends AbstractHierarchyTreeWidget<Message> {

	/**
	 * Factory.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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

	/**
	 * Constructs widget depending on whether it is root, inner, leaf node.
	 * 
	 * @param hierarchy
	 *            hierarchy node
	 */
	public MessageWidget(Hierarchy<Message> hierarchy) {
		super(hierarchy);

		if (hierarchy instanceof LeafNode) {
			createLeafNodePanel();
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel();
			} else {
				createRootPanel();
			}
		}
	}

	/**
	 * Create content in case it is root node.
	 */
	private void createRootPanel() {
		String clearTooltip =
			I18n.getHierarchyTreeConstants().clearMessagesTooltip();
		createAnchor(I18n.getHierarchyTreeConstants().messageRootText(), null,
			null, null);
		createImage(HierarchyResources.INSTANCE.clear(), clearHandler,
			clearTooltip);
	}

	/**
	 * Create content in case it is inner node.
	 */
	private void createInnerNodePanel() {
		createLabel(getHierarchy().getName(), null, null);
	}

	/**
	 * Create content in case it is leaf node.
	 */
	private void createLeafNodePanel() {
		LeafNode<Message> leaf = (LeafNode<Message>) getHierarchy();
		final Message item = leaf.getItem();

		InlineLabel messageLabel =
			createLabel(item.getMessageBody(), new ClickHandler() {
				public void onClick(ClickEvent event) {
					MessagesManager.MESSENGER.addMessage(item, false);
				}
			}, null);
		messageLabel.setStylePrimaryName("message");
		messageLabel.setStyleDependentName(item.getMessageType().name(), true);
	}

}
