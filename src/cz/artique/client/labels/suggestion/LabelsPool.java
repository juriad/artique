package cz.artique.client.labels.suggestion;

import java.util.List;

import cz.artique.shared.model.label.Label;

public interface LabelsPool {
	boolean isNewValueAllowed();

	List<Label> fullTextSearch(String text);
}
