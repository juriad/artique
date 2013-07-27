package cz.artique.client.items;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.labels.AbstractLabelsBar;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.suggestion.LabelsPool;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

/**
 * List of labels to be assigned when new manual item is created.
 * 
 * @see AbstractLabelsBar
 * @author Adam Juraszek
 * 
 */
public class ManualItemLabelsBar extends AbstractLabelsBar {

	/**
	 * Allow user to add new label and select labels of those user defined.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	private static class MyLabelsPool implements LabelsPool {
		public boolean isNewValueAllowed() {
			return true;
		}

		public List<Label> fullTextSearch(String text) {
			return Managers.LABELS_MANAGER.fullTextSearch(text,
				Managers.LABELS_MANAGER.getLabels(LabelType.USER_DEFINED));
		}
	}

	public ManualItemLabelsBar() {
		super(new MyLabelsPool());
	}

	@Override
	protected void labelSelected(final Label label) {
		if (getLabels().contains(label)) {
			removeLabel(label);
		} else {
			addLabel(label);
		}
	}

	@Override
	protected void labelRemoved(final LabelWidget labelWidget) {
		removeLabel(labelWidget);
	}

	@Override
	protected void newLabelSelected(final String name) {
		Label labelByName =
			Managers.LABELS_MANAGER
				.getLabelByName(LabelType.USER_DEFINED, name);
		if (labelByName != null) {
			labelSelected(labelByName);
		} else {
			Managers.LABELS_MANAGER.createNewLabel(name,
				new AsyncCallback<Label>() {
					public void onFailure(Throwable caught) {
						// do nothing
					}

					public void onSuccess(Label result) {
						labelSelected(result);
					}
				});
		}
	}

	/**
	 * Sets list of labels to be prefilled in the bar.
	 * 
	 * @param labels
	 *            list of labels
	 */
	public void setNewData(List<Key> labels) {
		if (labels == null) {
			labels = new ArrayList<Key>();
		}
		List<Label> newList = Managers.LABELS_MANAGER.getSortedList(labels);

		removeAllLabels();

		for (Label l : newList) {
			if (LabelType.USER_DEFINED.equals(l.getLabelType())) {
				addLabel(l);
			}
		}
	}

	@Override
	protected LabelWidget createWidget(Label label) {
		return LabelWidget.FACTORY.createWidget(label);
	}

}
