package cz.artique.client.labels;

import com.google.gwt.user.client.ui.IsWidget;

public interface ComparableWidget<E extends ComparableWidget<E>>
		extends Comparable<E>, IsWidget {}
