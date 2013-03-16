package cz.artique.client.artiqueLabels;

import com.google.gwt.user.client.ui.ValueLabel;

import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelSuggestionFactory
		implements SuggesionLabelFactory<Label> {

	public static final ArtiqueLabelSuggestionFactory factory =
		new ArtiqueLabelSuggestionFactory();

	private final LabelRenderer renderer = new LabelRenderer(true);

	public ValueLabel<Label> createLabel() {
		return new ValueLabel<Label>(renderer);
	}

}
