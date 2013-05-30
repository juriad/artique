package cz.artique.client.labels.suggestion;

import com.google.gwt.user.client.ui.ValueLabel;

import cz.artique.client.labels.LabelRenderer;
import cz.artique.shared.model.label.Label;

public class LabelSuggestionFactory {

	public static final LabelSuggestionFactory FACTORY =
		new LabelSuggestionFactory();

	private final LabelRenderer renderer = new LabelRenderer(true);

	public ValueLabel<Label> createLabel() {
		return new ValueLabel<Label>(renderer);
	}

}
