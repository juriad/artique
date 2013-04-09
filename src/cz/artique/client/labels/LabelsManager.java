package cz.artique.client.labels;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.manager.Manager;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.utils.HasDisplayName;
import cz.artique.shared.utils.HasKey;

public interface LabelsManager<E extends HasDisplayName & HasKey<K>, K>
		extends Manager {
	List<E> getLabels(LabelType filterType);

	E getLabelByKey(K key);

	void createNewLabel(String name, AsyncCallback<E> ping);

	List<E> getSortedList(List<K> keys);

	List<E> fullTextSearch(String text, List<E> allLabels);

	E getLabelByName(LabelType type, String name);

}
