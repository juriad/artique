package cz.artique.client.labels;

import cz.artique.shared.model.label.Label;

/**
 * Factory of {@link LabelWidget}s.
 * 
 * @author Adam Juraszek
 * 
 */
public interface LabelWidgetFactory {
	LabelWidget createWidget(Label label);
}
