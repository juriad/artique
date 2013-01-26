package cz.artique.client.artiqueLabels;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueLabel;

import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelSuggestionFactory
		implements SuggesionLabelFactory<Label> {

	public static final ArtiqueLabelSuggestionFactory factory =
		new ArtiqueLabelSuggestionFactory();

	public ValueLabel<Label> createLabel() {
		// TODO rozsirit
		return new ValueLabel<Label>(new AbstractRenderer<Label>() {

			public String render(Label label) {
				switch (label.getLabelType()) {
				case SYSTEM:
					if (label.getName().equalsIgnoreCase("AND")) {
						return ArtiqueI18n.I18N.getConstants().operatorAnd();
					} else if (label.getName().equalsIgnoreCase("OR")) {
						return ArtiqueI18n.I18N.getConstants().operatorOr();
					} else {
						return "ERROR: unknown system label";
					}
				case USER_DEFINED:
					return label.getName();
				case USER_SOURCE:
					ArtiqueSourcesManager.MANAGER.getByLabel(label);
				default:
					return "ERROR: unknown label type";
				}
			}
		});
	}

}
