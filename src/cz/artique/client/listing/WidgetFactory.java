package cz.artique.client.listing;

import com.google.gwt.user.client.ui.Widget;

public interface WidgetFactory<E> {
	Widget createWidget(E e);
}
