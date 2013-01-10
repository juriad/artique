package cz.artique.client.listing2;

import cz.artique.shared.utils.HasKey;

public interface RowWidgetFactory<E extends HasKey<K>, K> {
	RowWidget<E, K> createWidget(E data);
}
