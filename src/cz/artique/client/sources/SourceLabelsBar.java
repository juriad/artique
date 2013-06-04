package cz.artique.client.sources;

import java.util.List;

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
			return false;
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
		}
		// do not create a new label
	}

	public UserSource getItem() {
		return source;
	}

	public void setNewData(UserSource userSource) {
		this.source = userSource;
		List<Label> newList =
			Managers.LABELS_MANAGER
				.getSortedList(userSource.getDefaultLabels());

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
