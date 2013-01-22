package cz.artique.client.artiqueLabels;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueLabel;

import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelSuggestionFactory
		implements SuggesionLabelFactory<Label> {
	
	public ValueLabel<Label> createLabel() {
		// TODO rozsirit
		return new ValueLabel<Label>(new AbstractRenderer<Label>() {

			public String render(Label object) {
				return object.getName();
			}
		});
	}

}
