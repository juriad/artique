package cz.artique.client.artiqueLabels;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.Ping;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelsBar;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class ArtiqueLabelsBar extends LabelsBar<Label, Key> {

	private final UserItem item;

	public ArtiqueLabelsBar(UserItem item, int maxSize) {
		super(ArtiqueLabelsManager.MANAGER, ArtiqueLabelWidget.factory, maxSize);
		this.item = item;
	}

	@Override
	protected void labelAdded(Label label) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void labelRemoved(LabelWidget<Label> labelWidget) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void newLabelAdded(final String name) {
		manager.createNewLabel(name, new Ping() {
			public void pong(boolean success) {
				if (!success) {
					// TODO osetreni chyby
					return;
				}
				Label created = manager.getLabelByName(name);
				if (created != null) {
					labelAdded(created);
				}
			}
		});
	}

	public UserItem getItem() {
		return item;
	}
	
	public void setNewData(List<Key> labels) {
		
	}

}
