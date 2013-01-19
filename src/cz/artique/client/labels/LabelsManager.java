package cz.artique.client.labels;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.Manager;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public interface LabelsManager<E extends HasName & HasKey<K>, K>
		extends Manager {
	List<E> getLabels();
	
	List<E> getUserDefinedLabels();

	E getLabelByName(String name);

	E getLabelByKey(K key);

	void createNewLabel(String name, AsyncCallback<E> ping);

	List<E> getSortedList(List<K> keys);
}
