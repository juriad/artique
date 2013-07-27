package cz.artique.client.labels.suggestion;

import java.util.List;

import cz.artique.client.labels.AbstractLabelsBar;
import cz.artique.shared.model.label.Label;

/**
 * Pool of available {@link Label}s which can be shown in
 * {@link AbstractLabelsBar}.
 * It also specifies, if the user is allowed in to create a new {@link Label}.
 * 
 * @author Adam Juraszek
 * 
 */
public interface LabelsPool {
	/**
	 * @return whether the user may create a new {@link Label}.
	 */
	boolean isNewValueAllowed();

	/**
	 * @param text
	 *            part of {@link Label} name
	 * @return list of {@link Label}s which matches the name
	 */
	List<Label> fullTextSearch(String text);
}
