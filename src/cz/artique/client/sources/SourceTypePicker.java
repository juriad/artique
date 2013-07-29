package cz.artique.client.sources;

import com.google.gwt.uibinder.client.UiConstructor;

import cz.artique.client.common.EnumRadioPicker;
import cz.artique.client.i18n.I18n;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.SourceType;

/**
 * Shows all sources types as a set of radio buttons.
 * 
 * @author Adam Juraszek
 * 
 */
public class SourceTypePicker extends EnumRadioPicker<SourceType> {

	@UiConstructor
	public SourceTypePicker() {
		super(SourceType.RSS_ATOM, "sourceType", I18n.getSourcesConstants());
	}

	/**
	 * {@link ManualSource} may not be selected.
	 * 
	 * @see cz.artique.client.common.EnumRadioPicker#canBeSelected(java.lang.Enum)
	 */
	@Override
	protected boolean canBeSelected(SourceType enu) {
		if (SourceType.MANUAL.equals(enu)) {
			return false;
		}
		return super.canBeSelected(enu);
	}
}
