package cz.artique.client.labels;

import com.google.gwt.user.client.ui.HasEnabled;

public interface LabelWidget<E>
		extends HasRemoveHandlers, ComparableWidget<LabelWidget<E>>, HasEnabled {
	E getLabel();
}
