package cz.artique.client.labels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;

import cz.artique.client.common.CloseButton;
import cz.artique.client.common.InlineFlowPanel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelAppearance;

/**
 * Widget representing a {@link Label}; it contains the {@link Label} name and
 * {@link CloseButton}.
 * 
 * The {@link LabelWidget} is {@link Comparable} because it is added into
 * {@link AbstractLabelsBar}.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelWidget extends Composite
		implements HasCloseHandlers<LabelWidget>, Comparable<LabelWidget>,
		HasEnabled {
	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("LabelWidget.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	public static final ArtiqueLabelWidgetFactory FACTORY =
		new ArtiqueLabelWidgetFactory();

	/**
	 * Factory.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class ArtiqueLabelWidgetFactory implements LabelWidgetFactory {
		public LabelWidget createWidget(Label l) {
			return new LabelWidget(l);
		}
	}

	private final Label label;
	private boolean enabled = true;
	private InlineFlowPanel panel;

	private CloseButton<LabelWidget> closeButton;

	protected final Anchor nameLabel;

	private final LabelRenderer renderer = new LabelRenderer(false);

	public LabelWidget(final Label label) {
		res.style().ensureInjected();
		this.label = label;
		panel = new InlineFlowPanel();
		initWidget(panel);
		setStylePrimaryName("label");

		nameLabel = new Anchor(renderer.render(label));
		panel.add(nameLabel);
		nameLabel.setStylePrimaryName("label-name");

		closeButton = new CloseButton<LabelWidget>(this);
		panel.add(closeButton);

		setStyle();
	}

	/**
	 * Sets foreground and background color.
	 */
	private void setStyle() {
		if (label.getAppearance() != null) {
			LabelAppearance appearance = label.getAppearance();
			if (appearance.getForegroundColor() != null) {
				Style style = nameLabel.getElement().getStyle();
				style.setColor(appearance.getForegroundColor());
			}
			if (appearance.getBackgroundColor() != null) {
				Style style = this.getElement().getStyle();
				style.setBackgroundColor(appearance.getBackgroundColor());
			}
		}
	}

	public HandlerRegistration addCloseHandler(CloseHandler<LabelWidget> handler) {
		return closeButton.addCloseHandler(handler);
	}

	/**
	 * @return backed {@link Label}
	 */
	public Label getLabel() {
		return label;
	}

	public int compareTo(LabelWidget o) {
		return getLabel().compareTo(o.getLabel());
	}

	/**
	 * Returns whether the close button is enabled.
	 * 
	 * @see com.google.gwt.user.client.ui.HasEnabled#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Disables close button.
	 * 
	 * @see com.google.gwt.user.client.ui.HasEnabled#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		closeButton.setEnabled(enabled);
	}
}
