package cz.artique.client.sources;

import java.util.List;

import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public interface SourcesManager<E extends HasName & HasKey<K>, K> {
	List<E> getSources();

	E getSourceByName(String name);

	E getSourceByKey(K key);

	// void createNewSource(String name, Ping ping);

}
