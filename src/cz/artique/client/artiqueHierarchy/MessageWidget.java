package cz.artique.client.artiqueHierarchy;

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
import cz.artique.client.i18n.ArtiqueI18n;
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

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		if (hierarchy instanceof LeafNode) {
			createLeafNodePanel(panel);
		} else if (hierarchy instanceof InnerNode) {
			if (hierarchy.getParent() != null) {
				createInnerNodePanel(panel);
			} else {
				createRootPanel(panel);
			}
		}
	}

	private void createRootPanel(FlowPanel panel) {
		String clearTooltip =
			ArtiqueI18n.I18N.getConstants().clearMessagesTooltip();
		createLabel(panel, "/", null, null);
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
			createLabel(panel, item.getMessageBody(), null, null);
		messageLabel.setStylePrimaryName("message");
		messageLabel.setStyleDependentName(item.getMessageType().name(), true);
	}

}
