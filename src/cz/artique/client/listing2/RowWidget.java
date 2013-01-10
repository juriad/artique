package cz.artique.client.listing2;

import com.google.gwt.user.client.ui.IsWidget;

import cz.artique.shared.utils.HasKey;

public interface RowWidget<E extends HasKey<K>, K>
		extends HasKey<K>, IsWidget, HasExpandCollapseHandlers {
	void setNewData(E e);

	E getData(boolean shown);

	K getKey();

	void expand();

	void collapse();

	boolean isExpanded();

}
