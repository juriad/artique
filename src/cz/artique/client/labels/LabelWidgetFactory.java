package cz.artique.client.labels;

import cz.artique.shared.model.label.Label;

public interface LabelWidgetFactory {
	LabelWidget createWidget(Label label);
}
