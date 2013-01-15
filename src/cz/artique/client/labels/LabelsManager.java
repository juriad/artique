package cz.artique.client.labels;

import java.util.List;

import cz.artique.client.Ping;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public interface LabelsManager<E extends HasName & HasKey<K>, K> {
	List<E> getLabels();

	E getLabelByName(String name);

	E getLabelByKey(K key);

	void createNewLabel(String name, Ping ping);

	List<E> getSortedList(List<K> keys);
}
