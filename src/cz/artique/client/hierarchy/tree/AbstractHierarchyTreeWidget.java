package cz.artique.client.hierarchy.tree;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.shared.utils.HasHierarchy;

/**
 * Abstract widget meant to be used inside {@link TreeItem}.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of hierarchy
 */
public abstract class AbstractHierarchyTreeWidget<E extends HasHierarchy>
		extends Composite implements HierarchyTreeWidget<E> {

	private Hierarchy<E> hierarchy;

	/**
	 * @param hierarchy
	 *            wrapped hierarchy node
	 */
	public AbstractHierarchyTreeWidget(Hierarchy<E> hierarchy) {
		this.hierarchy = hierarchy;
		panel = new FlowPanel();
		initWidget(panel);
		setStylePrimaryName("hierarchyWidget");
	}

	public boolean refresh() {
		// do nothing
		return true;
	}

	public Hierarchy<E> getHierarchy() {
		return hierarchy;
	}

	/**
	 * Creates anchor and adds it to panel
	 * 
	 * @param name
	 *            name of anchor
	 * @param token
	 *            href hash
	 * @param clickHandler
	 *            click handler
	 * @param tooltip
	 *            tooltip
	 * @return created anchor
	 */
	protected Anchor createAnchor(String name, String token,
			ClickHandler clickHandler, String tooltip) {
		Anchor anchor = new Anchor(name);
		panel.add(anchor);
		if (token != null) {
			anchor.setHref("#" + token);
		}
		if (clickHandler != null) {
			anchor.addClickHandler(clickHandler);
		}
		if (tooltip != null) {
			anchor.setTitle(tooltip);
		}
		return anchor;
	}

	/**
	 * Creates inline label and adds it to panel
	 * 
	 * @param name
	 *            name of anchor
	 * @param clickHandler
	 *            click handler
	 * @param tooltip
	 *            tooltip
	 * @return created inline label
	 */
	protected InlineLabel createLabel(String name, ClickHandler clickHandler,
			String tooltip) {
		InlineLabel label = new InlineLabel(name);
		panel.add(label);
		if (clickHandler != null) {
			label.addClickHandler(clickHandler);
		}
		if (tooltip != null) {
			label.setTitle(tooltip);
		}
		return label;
	}

	/**
	 * Creates image and adds it to panel
	 * 
	 * @param resource
	 *            resource of the image
	 * @param clickHandler
	 *            click handler
	 * @param tooltip
	 *            tooltip
	 * @return created image
	 */
	protected Image createImage(ImageResource resource,
			ClickHandler clickHandler, String tooltip) {
		Image image;
		if (resource != null) {
			image = new Image(resource);
		} else {
			image = new Image();
		}
		image.setStylePrimaryName("hiddenAction");
		panel.add(image);
		if (clickHandler != null) {
			image.addClickHandler(clickHandler);
		}
		if (tooltip != null) {
			image.setTitle(tooltip);
		}
		return image;
	}

	protected boolean selected = false;
	private final FlowPanel panel;

	public void setSelected(boolean selected) {
		this.selected = selected;
		this.setStyleDependentName("selected", selected);
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	protected FlowPanel getWidget() {
		return panel;
	}
}
