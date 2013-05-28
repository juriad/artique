package cz.artique.client.artiqueHierarchy;

import com.google.code.gwteyecandy.Tooltip;
import com.google.code.gwteyecandy.Tooltip.TooltipPosition;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasName;

public class AbstractHierarchyTreeWidget<E extends HasHierarchy & HasName>
		extends Composite implements HierarchyTreeWidget<E> {

	private Hierarchy<E> hierarchy;

	public AbstractHierarchyTreeWidget(Hierarchy<E> hierarchy) {
		this.hierarchy = hierarchy;
	}

	public boolean refresh() {
		// do nothing
		return true;
	}

	public Hierarchy<E> getHierarchy() {
		return hierarchy;
	}

	protected Anchor createAnchor(FlowPanel panel, String name, String token,
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
			Tooltip tt = addTooltip(tooltip);
			tt.attachTo(anchor);
		}
		return anchor;
	}

	protected InlineLabel createLabel(FlowPanel panel, String name,
			ClickHandler clickHandler, String tooltip) {
		InlineLabel label = new InlineLabel(name);
		panel.add(label);
		if (clickHandler != null) {
			label.addClickHandler(clickHandler);
		}
		if (tooltip != null) {
			Tooltip tt = addTooltip(tooltip);
			tt.attachTo(label);
		}
		return label;
	}

	protected Image createImage(FlowPanel panel, ImageResource resource,
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
			Tooltip tt = addTooltip(tooltip);
			tt.attachTo(image);
		}
		return image;
	}

	protected Tooltip addTooltip(String tooltip) {
		Tooltip tt = new Tooltip();
		tt.setPosition(TooltipPosition.RIGHT_MIDDLE);
		tt.setText(tooltip);
		return tt;
	}

	protected boolean selected = false;

	public void setSelected(boolean selected) {
		this.selected = selected;
		this.setStyleDependentName("selected", selected);
	}

	public boolean isSelected() {
		return selected;
	}
}
