package cz.artique.client.labels;

import com.google.gwt.cell.client.Cell;

import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public interface LabelCell<E extends HasName & HasKey<K>, K> extends Cell<E> {

}
