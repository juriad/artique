package cz.artique.client.artiqueHierarchy;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.messages.Message;

public class MessageWidget extends Composite
		implements HierarchyTreeWidget<Message> {

	public static class MessageWidgetFactory
			implements HierarchyTreeWidgetFactory<Message> {

		public static final HierarchyTreeWidgetFactory<Message> FACTORY =
			new MessageWidgetFactory();

		public HierarchyTreeWidget<Message> createWidget(
				Hierarchy<Message> hierarchy) {
			return new MessageWidget(hierarchy);
		}
	}

	private Hierarchy<Message> hierarchy;

	public MessageWidget(Hierarchy<Message> hierarchy) {
		this.hierarchy = hierarchy;

		if (hierarchy instanceof LeafNode) {
			LeafNode<Message> leaf = (LeafNode<Message>) hierarchy;
			final Message item = leaf.getItem();

			Label messageLabel = new Label(item.getMessageBody());
			messageLabel.setStylePrimaryName("message");
			messageLabel.setStyleDependentName(item.getMessageType().name(),
				true);

			initWidget(messageLabel);
		} else {
			Label label = new Label("/");
			initWidget(label);
		}
	}

	public void refresh() {
		// do nothing
	}

	public Hierarchy<Message> getHierarchy() {
		return hierarchy;
	}

}
