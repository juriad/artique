package cz.artique.client.listing.row;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.labels.AbstractLabelsBar;
import cz.artique.client.labels.ClickableLabelWidget;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.suggestion.LabelsPool;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

/**
 * {@link AbstractLabelsBar} implementation shown in the {@link UserItemRow}
 * header.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserItemLabelsBar extends AbstractLabelsBar {

	private UserItem item;

	/**
	 * Allow new {@link Label}s and search only user-deined {@link Label}s.
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

	public UserItemLabelsBar() {
		super(new MyLabelsPool());
	}

	@Override
	protected void labelSelected(final Label label) {
		if (getLabels().contains(label)) {
			Managers.ITEMS_MANAGER.labelRemoved(getItem(), label, null);
			removeLabel(label);
		} else {
			Managers.ITEMS_MANAGER.labelAdded(getItem(), label, null);
			addLabel(label);
		}
	}

	@Override
	protected void labelRemoved(final LabelWidget labelWidget) {
		Managers.ITEMS_MANAGER.labelRemoved(getItem(), labelWidget.getLabel(),
			null);
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
	 * @return {@link UserItem} which this {@link UserItemLabelsBar} shows
	 *         {@link Label}s for
	 */
	public UserItem getItem() {
		return item;
	}

	/**
	 * Sets new data (new list of {@link Label}s)
	 * 
	 * @param userItem
	 *            new data
	 */
	public void setNewData(final UserItem userItem) {
		onSuggestionClosed(new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {}

			public void onSuccess(Void result) {
				doSetNewData(userItem);
			}
		});
	}

	/**
	 * Does the actual setting of new data.
	 * 
	 * @param userItem
	 *            new data
	 */
	private void doSetNewData(UserItem userItem) {
		this.item = userItem;
		List<Key> labels2 = userItem.getLabels();
		if (labels2 == null) {
			labels2 = new ArrayList<Key>();
		}
		List<Label> newList = Managers.LABELS_MANAGER.getSortedList(labels2);
		List<Label> oldList = new ArrayList<Label>(getLabels());

		List<Label> newListCopy = new ArrayList<Label>(newList);
		newListCopy.removeAll(oldList);
		for (Label l : newListCopy) {
			if (LabelType.USER_DEFINED.equals(l.getLabelType())) {
				addLabel(l);
			}
		}

		oldList.removeAll(newList);
		for (Label l : oldList) {
			removeLabel(l);
		}
	}

	@Override
	protected LabelWidget createWidget(Label label) {
		return ClickableLabelWidget.FACTORY.createWidget(label);
	}
}
