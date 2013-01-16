package cz.artique.client.artiqueLabels;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.artiqueItems.ArtiqueItemsManager;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelsBar;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelsBar extends LabelsBar<Label, Key> {

	private UserItem item;

	public ArtiqueLabelsBar(UserItem item, int maxSize) {
		super(ArtiqueLabelsManager.MANAGER, ArtiqueLabelWidget.factory, maxSize);
		this.item = item;
	}

	@Override
	protected void labelAdded(final Label label) {
		ArtiqueItemsManager.MANAGER.labelAdded(getItem(), label,
			new AsyncCallback<Void>() {

				public void onSuccess(Void result) {
					addLabel(label);
				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
	}

	@Override
	protected void labelRemoved(final LabelWidget<Label> labelWidget) {
		ArtiqueItemsManager.MANAGER.labelRemoved(getItem(),
			labelWidget.getLabel(), new AsyncCallback<Void>() {

				public void onSuccess(Void result) {
					removeLabel(labelWidget);
				}

				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
	}

	@Override
	protected void newLabelAdded(final String name) {
		manager.createNewLabel(name, new AsyncCallback<Label>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(Label result) {
				labelAdded(result);
			}
		});
	}

	public UserItem getItem() {
		return item;
	}

	public void setNewData(UserItem userItem) {
		this.item = userItem;
		List<Label> newList = manager.getSortedList(userItem.getLabels());
		List<Label> oldList = manager.getSortedList(userItem.getLabels());
		
		List<Label> newListCopy = new ArrayList<Label>(newList);
		newListCopy.removeAll(oldList);
		for(Label l: newListCopy) {
			addLabel(l);
		}
		
		oldList.removeAll(newList);
		for(Label l: oldList) {
			removeLabel(l);
		}
	}
}
