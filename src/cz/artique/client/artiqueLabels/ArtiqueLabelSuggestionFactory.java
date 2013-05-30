package cz.artique.client.artiqueLabels;

import com.google.gwt.user.client.ui.ValueLabel;

import cz.artique.shared.model.label.Label;

public class ArtiqueLabelSuggestionFactory {

	public static final ArtiqueLabelSuggestionFactory FACTORY =
		new ArtiqueLabelSuggestionFactory();

	private final LabelRenderer renderer = new LabelRenderer(true);

	public ValueLabel<Label> createLabel() {
		return new ValueLabel<Label>(renderer);
	}

}
