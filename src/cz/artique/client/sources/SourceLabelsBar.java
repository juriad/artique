package cz.artique.client.sources;

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
import cz.artique.shared.model.source.UserSource;

public class SourceLabelsBar extends AbstractLabelsBar {

	private UserSource source;

	private static class MyLabelsPool implements LabelsPool {
		public boolean isNewValueAllowed() {
			return true;
		}

		public List<Label> fullTextSearch(String text) {
			return Managers.LABELS_MANAGER.fullTextSearch(text,
				Managers.LABELS_MANAGER.getLabels(LabelType.USER_DEFINED));
		}
	}

	public SourceLabelsBar() {
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
		if (labelByName != null) { // try again
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

	public UserSource getItem() {
		return source;
	}

	public void setNewData(UserSource userSource) {
		this.source = userSource;
		List<Key> defaultLabels = userSource.getDefaultLabels();
		if (defaultLabels == null) {
			defaultLabels = new ArrayList<Key>();
		}
		List<Label> newList =
			Managers.LABELS_MANAGER.getSortedList(defaultLabels);

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
