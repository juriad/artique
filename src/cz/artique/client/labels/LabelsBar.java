package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.labels.ClickableLabelWidget;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.AbstractLabelsBar;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

public class LabelsBar extends AbstractLabelsBar {

	private UserItem item;

	public LabelsBar(UserItem item) {
		super(ClickableLabelWidget.FACTORY);
		this.item = item;

		for (Key key : item.getLabels()) {
			Label label = Managers.LABELS_MANAGER.getLabelByKey(key);
			if (LabelType.USER_DEFINED.equals(label.getLabelType())) {
				addLabel(label);
			}
		}
	}

	@Override
	protected void labelAdded(final Label label) {
		Managers.ITEMS_MANAGER.labelAdded(getItem(), label, null);
		addLabel(label);
	}

	@Override
	protected void labelRemoved(final LabelWidget labelWidget) {
		Managers.ITEMS_MANAGER.labelRemoved(getItem(), labelWidget.getLabel(),
			null);
		removeLabel(labelWidget);
	}

	@Override
	protected void newLabelAdded(final String name) {

		Label labelByName =
			Managers.LABELS_MANAGER
				.getLabelByName(LabelType.USER_DEFINED, name);
		if (labelByName != null) {
			labelAdded(labelByName);
		} else {
			Managers.LABELS_MANAGER.createNewLabel(name,
				new AsyncCallback<Label>() {
					public void onFailure(Throwable caught) {
						// already handled
					}

					public void onSuccess(Label result) {
						labelAdded(result);
					}
				});
		}
	}

	public UserItem getItem() {
		return item;
	}

	public void setNewData(UserItem userItem) {
		this.item = userItem;
		List<Label> newList =
			Managers.LABELS_MANAGER.getSortedList(userItem.getLabels());
		List<Label> oldList =
			Managers.LABELS_MANAGER.getSortedList(userItem.getLabels());

		List<Label> newListCopy = new ArrayList<Label>(newList);
		newListCopy.removeAll(oldList);
		for (Label l : newListCopy) {
			addLabel(l);
		}

		oldList.removeAll(newList);
		for (Label l : oldList) {
			removeLabel(l);
		}
	}
}
