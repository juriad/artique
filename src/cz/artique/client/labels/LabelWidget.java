package cz.artique.client.labels;

import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;

import cz.artique.client.artiqueHistory.CachingHistoryUtils;
import cz.artique.client.artiqueLabels.LabelRenderer;
import cz.artique.client.common.CloseButton;
import cz.artique.client.common.InlineFlowPanel;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;

public class LabelWidget extends Composite
		implements HasCloseHandlers<LabelWidget>, Comparable<LabelWidget>,
		HasEnabled {

	public static final ArtiqueLabelWidgetFactory FACTORY =
		new ArtiqueLabelWidgetFactory();

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
		this.label = label;
		panel = new InlineFlowPanel();
		initWidget(panel);
		setStylePrimaryName("label");

		nameLabel = new Anchor(renderer.render(label));
		panel.add(nameLabel);
		nameLabel.setStylePrimaryName("label-name");

		closeButton = new CloseButton<LabelWidget>(this);
		panel.add(closeButton);

		Filter filter = CachingHistoryUtils.UTILS.getFilterForLabel(label);
		String serialized =
			CachingHistoryUtils.UTILS.serializeListFilter(filter);
		nameLabel.setHref("#" + serialized);
	}

	public HandlerRegistration addCloseHandler(CloseHandler<LabelWidget> handler) {
		return closeButton.addCloseHandler(handler);
	}

	public Label getLabel() {
		return label;
	}

	public int compareTo(LabelWidget o) {
		return getLabel().compareTo(o.getLabel());
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		closeButton.setEnabled(enabled);
	}
}
