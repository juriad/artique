package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

import cz.artique.client.labels.LabelsManager;
import cz.artique.client.listing.NewDataEvent.NewDataType;
import cz.artique.client.listing.row.RowWidget;
import cz.artique.client.listing.row.UserItemRow;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

/**
 * Represents the potentially infinite list of {@link UserItem}s.
 * The {@link UserItem}s are provided by {@link InfiniteListDataProvider}.
 * 
 * @author Adam Juraszek
 * 
 */
public class InfiniteList extends Composite
		implements HasSelectionChangedHandlers, HasScrollEndHandlers,
		HasNewDataHandlers {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("InfiniteList.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	/**
	 * Change selection when user opens/closes a row.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	private class OpenCloseHander
			implements CloseHandler<RowWidget>, OpenHandler<RowWidget> {
		public void onClose(CloseEvent<RowWidget> event) {
			setSelectedKey(event.getTarget().getKey(), true);
		}

		public void onOpen(OpenEvent<RowWidget> event) {
			setSelectedKey(event.getTarget().getKey(), true);
		}
	}

	private final FlowPanel flowPanel;
	private Map<Key, RowWidget> rows;
	private Set<Key> headKeys = new HashSet<Key>();
	private Set<Key> tailKeys = new HashSet<Key>();

	private Key selected = null;

	private List<UserItem> head;
	private List<UserItem> tail;
	private boolean endReached;

	protected final ScrollPanel scrollPanel;
	private InfiniteListDataProvider provider;

	public InfiniteList() {
		res.style().ensureInjected();
		flowPanel = new FlowPanel();
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);
		scrollPanel.add(flowPanel);
		flowPanel.setStylePrimaryName("infiniteList");
		clear();
	}

	/**
	 * @return number of displayed rows
	 */
	public int getRowCount() {
		return rows.size();
	}

	public HandlerRegistration addScrollEndHandler(ScrollEndHandler handler) {
		return addHandler(handler, ScrollEndEvent.getType());
	}

	/**
	 * Adds values to list of {@link UserItem}s which are below the last shown
	 * {@link UserItem}.
	 * 
	 * @param values
	 *            list of {@link UserItem}s
	 */
	public void appendValues(List<UserItem> values) {
		if (values.size() > 0) {
			List<UserItem> newItems = new ArrayList<UserItem>();
			Set<Key> newKeys = new HashSet<Key>();
			for (UserItem ui : values) {
				RowWidget rowWidget = rows.get(ui.getKey());
				boolean add = true;
				if (rowWidget != null) {
					setValue(ui);
					add = false;
				}
				if (add) {
					if (tailKeys.contains(ui.getKey())) {
						add = false;
						int index = tail.indexOf(ui);
						tail.set(index, ui);
					}
				}
				if (add) {
					newItems.add(ui);
					newKeys.add(ui.getKey());
				}
			}

			if (!newItems.isEmpty()) {
				tail.addAll(0, newItems);
				tailKeys.addAll(newKeys);
				NewDataEvent.fire(this, NewDataType.NEW_DATA_AVAILABLE);
			}
		}
	}

	/**
	 * Adds values to list of {@link UserItem}s which are above the first shown
	 * {@link UserItem}.
	 * 
	 * @param values
	 *            list of {@link UserItem}s
	 */
	public void prependValues(List<UserItem> values) {
		if (values.size() > 0) {
			List<UserItem> newItems = new ArrayList<UserItem>();
			Set<Key> newKeys = new HashSet<Key>();
			for (UserItem ui : values) {
				RowWidget rowWidget = rows.get(ui.getKey());
				boolean add = true;
				if (rowWidget != null) {
					setValue(ui);
					add = false;
				}
				if (add) {
					if (headKeys.contains(ui.getKey())) {
						add = false;
						int index = head.indexOf(ui);
						head.set(index, ui);
					}
				}
				if (add) {
					newItems.add(ui);
					newKeys.add(ui.getKey());
				}
			}

			if (!newItems.isEmpty()) {
				head.addAll(0, newItems);
				headKeys.addAll(newKeys);
				NewDataEvent.fire(this, NewDataType.NEW_DATA_AVAILABLE);
			}
		}
	}

	/**
	 * @return number of {@link UserItem}s above the first shown
	 *         {@link UserItem}
	 */
	public int getAvailableHeadSize() {
		return head.size();
	}

	/**
	 * @return number of {@link UserItem}s below the first shown
	 *         {@link UserItem}
	 */
	public int getAvailableTailSize() {
		return tail.size();
	}

	/**
	 * Sets new value of row if the {@link UserItem} is already shown.
	 * 
	 * @param value
	 *            new value to show
	 */
	public void setValue(final UserItem value) {
		if (!hasAllLabels(value.getLabels())) {
			Managers.LABELS_MANAGER.refresh(new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					doSetValue(value);
				}

				public void onFailure(Throwable caught) {}
			});
		} else {
			doSetValue(value);
		}
	}

	/**
	 * Does the actual value setting.
	 * 
	 * @param value
	 *            new value to show
	 */
	private void doSetValue(UserItem value) {
		RowWidget row = rows.get(value.getKey());
		if (row != null) {
			Item itemObject = row.getValue().getItemObject();
			value.setItemObject(itemObject);
			row.setValue(value);
		}
	}

	/**
	 * Makes all pending {@link UserItem}s above the first one show.
	 */
	public void showHead() {
		Set<Key> labels = new HashSet<Key>();
		for (UserItem ui : head) {
			labels.addAll(ui.getLabels());
		}
		if (!hasAllLabels(labels)) {
			Managers.LABELS_MANAGER.refresh(new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					addHead();
				}

				public void onFailure(Throwable caught) {}
			});
		} else {
			addHead();
		}
	}

	/**
	 * Does the actual addition of {@link UserItem}s to the top of the list.
	 */
	private void addHead() {
		List<UserItem> l = head;
		if (l.size() > 0) {
			head = new ArrayList<UserItem>();
			headKeys = new HashSet<Key>();

			int offsetHeight = flowPanel.getOffsetHeight();
			for (int i = l.size() - 1; i >= 0; i--) {
				UserItem e = l.get(i);
				RowWidget row = createRow(e);
				flowPanel.insert(row, 0);
				rows.put(row.getKey(), row);
			}

			int deltaHeight = flowPanel.getOffsetHeight() - offsetHeight;
			scrollPanel.setVerticalScrollPosition(scrollPanel
				.getVerticalScrollPosition() + deltaHeight);

			rowsAdded();
			NewDataEvent.fire(this, NewDataType.NEW_DATA_SHOWN);
		}
	}

	/**
	 * Makes all pending {@link UserItem}s below the last one show.
	 */
	public void showTail() {
		Set<Key> labels = new HashSet<Key>();
		for (UserItem ui : tail) {
			labels.addAll(ui.getLabels());
		}
		if (!hasAllLabels(labels)) {
			Managers.LABELS_MANAGER.refresh(new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					addTail();
				}

				public void onFailure(Throwable caught) {}
			});
		} else {
			addTail();
		}
	}

	/**
	 * Does the actual addition of {@link UserItem}s to the top of the list.
	 */
	public void addTail() {
		List<UserItem> l = tail;
		if (l.size() > 0) {
			tail = new ArrayList<UserItem>();
			tailKeys = new HashSet<Key>();

			for (UserItem e : l) {
				RowWidget row = createRow(e);
				flowPanel.add(row);
				rows.put(row.getKey(), row);
			}

			rowsAdded();
			NewDataEvent.fire(this, NewDataType.NEW_DATA_SHOWN);
		}
	}

	/**
	 * Called when new rows are shown.
	 */
	protected void rowsAdded() {}

	/**
	 * Creates a new row for a {@link UserItem}.
	 * 
	 * @param userItem
	 *            {@link UserItem} which is the row created for
	 * @return widget representing the {@link UserItem}
	 */
	private RowWidget createRow(UserItem userItem) {
		RowWidget row = UserItemRow.FACTORY.createWidget(userItem);
		row.addOpenHandler(new OpenCloseHander());
		row.addCloseHandler(new OpenCloseHander());
		return row;
	}

	/**
	 * Clears all shown rows and all {@link UserItem}s.
	 */
	public void clear() {
		flowPanel.clear();
		rows = new HashMap<Key, RowWidget>();
		head = new ArrayList<UserItem>();
		tail = new ArrayList<UserItem>();
		headKeys = new HashSet<Key>();
		tailKeys = new HashSet<Key>();
		selected = null;
		endReached = false;
	}

	/**
	 * Sets the currently selected row by key of its {@link UserItem}.
	 * 
	 * @param key
	 *            key of {@link UserItem}
	 * @param forceExpand
	 *            whether the selected row shall be expanded
	 */
	public void setSelectedKey(Key key, boolean forceExpand) {
		if (key == null) {
			RowWidget old = rows.get(selected);
			old.collapse();
			old.removeStyleDependentName("selected");
			selected = null;
			SelectionChangeEvent.fire(this);
		} else if (key.equals(selected)) {
			// was selected and is selected
		} else {
			boolean expand = true;
			// selected changed
			if (selected != null) {
				RowWidget old = rows.get(selected);
				old.removeStyleDependentName("selected");
				if (old.isExpanded()) {
					old.collapse();
				} else {
					expand = false;
				}
			}
			RowWidget newly = rows.get(key);
			if (expand || forceExpand) {
				newly.expand();
			}
			selected = key;
			newly.addStyleDependentName("selected");
			SelectionChangeEvent.fire(this);
		}
	}

	/**
	 * @return currently selected {@link RowWidget} or null if none is selected
	 */
	public RowWidget getSelectedRowWidget() {
		return selected == null ? null : rows.get(selected);
	}

	/**
	 * @param index
	 *            index of row to be selected
	 * @param forceExpand
	 *            whether the selected row shall be expanded
	 * @return whether the index exists
	 */
	protected boolean setSelectedIndex(int index, boolean forceExpand) {
		try {
			RowWidget w = (RowWidget) flowPanel.getWidget(index);
			setSelectedKey(w.getKey(), forceExpand);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return index of currently selected row
	 */
	protected int getSelectedIndex() {
		return flowPanel.getWidgetIndex(getSelectedRowWidget());
	}

	/**
	 * @param index
	 *            index
	 * @return {@link RowWidget} by its index
	 */
	protected RowWidget getRow(int index) {
		return (RowWidget) flowPanel.getWidget(index);
	}

	public HandlerRegistration addSelectionChangeHandler(
			com.google.gwt.view.client.SelectionChangeEvent.Handler handler) {
		return addHandler(handler, SelectionChangeEvent.getType());
	}

	/**
	 * Sets a new {@link InfiniteListDataProvider} providing {@link UserItem}s.
	 * It also clears the list.
	 * 
	 * @param provider
	 *            new provider
	 */
	public void setProvider(InfiniteListDataProvider provider) {
		clear();
		this.provider = provider;
	}

	/**
	 * @return current provider of {@link UserItem}s
	 */
	public InfiniteListDataProvider getProvider() {
		return provider;
	}

	/**
	 * @return whether the end of list has been reached
	 */
	public boolean isEndReached() {
		return endReached;
	}

	/**
	 * @param endReached
	 *            whether the end of list has been reached
	 */
	public void setEndReached(boolean endReached) {
		this.endReached = endReached;
	}

	public HandlerRegistration addNewDataHandler(NewDataHandler handler) {
		return addHandler(handler, NewDataEvent.getType());
	}

	/**
	 * Tests whether all keys of {@link Label}s are known to
	 * {@link LabelsManager}.
	 * 
	 * @param keys
	 *            list of keys to be tested
	 * @return whether all keys are known to {@link LabelsManager}
	 */
	protected boolean hasAllLabels(Iterable<Key> keys) {
		boolean hasAll = true;
		for (Key key : keys) {
			if (Managers.LABELS_MANAGER.getLabelByKey(key) == null) {
				hasAll = false;
			}
		}
		return hasAll;
	}
}
