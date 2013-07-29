package cz.artique.client.sources;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;

/**
 * Selection of {@link SourceSource} by group of radio buttons.
 * 
 * @author Adam Juraszek
 * 
 */
public class SourceSourcePicker extends EnumRadioPicker<SourceSource> {

	public SourceSourcePicker() {
		super(SourceSource.CUSTOM, "sourceSource", I18n.getSourcesConstants());
	}

}
