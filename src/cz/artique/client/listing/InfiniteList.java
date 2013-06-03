package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;

public class InfiniteList extends Composite
		implements HasSelectionChangedHandlers, HasScrollEndHandlers,
		HasNewDataHandlers {

	protected OpenCloseHandler openCloseHandler = new OpenCloseHandler();

	protected class OpenCloseHandler
			implements OpenHandler<RowWidget>, CloseHandler<RowWidget> {

		public void onClose(CloseEvent<RowWidget> event) {
			RowWidget row = event.getTarget();
			setSelectedKey(row.getKey(), row.isExpanded());
		}

		public void onOpen(OpenEvent<RowWidget> event) {
			RowWidget row = event.getTarget();
			setSelectedKey(row.getKey(), row.isExpanded());
		}
	}

	private final FlowPanel flowPanel;
	private Map<Key, RowWidget> rows;
	private Key selected = null;

	private List<UserItem> head;
	private List<UserItem> tail;
	private boolean endReached;

	protected final ScrollPanel scrollPanel;
	private InfiniteListDataProvider provider;

	public InfiniteList() {
		flowPanel = new FlowPanel();
		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);
		scrollPanel.add(flowPanel);
		clear();
	}

	public int getRowCount() {
		return rows.size();
	}

	public HandlerRegistration addScrollEndHandler(ScrollEndHandler handler) {
		return addHandler(handler, ScrollEndEvent.getType());
	}

	public void appendValues(List<UserItem> values) {
		tail.addAll(values);
	}

	public void prependValues(List<UserItem> values) {
		head.addAll(0, values);
	}

	public void setValue(UserItem value) {
		RowWidget row = rows.get(value.getKey());
		if (row != null) {
			Item itemObject = row.getValue().getItemObject();
			value.setItemObject(itemObject);
			row.setValue(value);
		}
	}

	public int showHead() {
		List<UserItem> l = head;
		if (l.size() > 0) {
			head = new ArrayList<UserItem>();

			for (UserItem e : l) {
				RowWidget row = createRow(e);
				flowPanel.insert(row, 0);
				rows.put(row.getKey(), row);
			}

			NewDataEvent.fire(this);
			rowsAdded();

		}
		return l.size();
	}

	public int showTail() {
		List<UserItem> l = tail;
		if (l.size() > 0) {
			tail = new ArrayList<UserItem>();

			for (UserItem e : l) {
				RowWidget row = createRow(e);
				flowPanel.add(row);
				rows.put(row.getKey(), row);
			}

			NewDataEvent.fire(this);
			rowsAdded();
		}
		return l.size();
	}

	protected void rowsAdded() {}

	private RowWidget createRow(UserItem e) {
		RowWidget row = UserItemRow.FACTORY.createWidget(e);

		row.addOpenHandler(openCloseHandler);
		row.addCloseHandler(openCloseHandler);
		return row;
	}

	public void clear() {
		flowPanel.clear();
		rows = new HashMap<Key, RowWidget>();
		head = new ArrayList<UserItem>();
		tail = new ArrayList<UserItem>();
		selected = null;
		endReached = false;
	}

	public void setSelectedKey(Key key, boolean isSelected) {
		if (!isSelected) {
			if (key == null) {
				// null is not selected
			} else if (key.equals(selected)) {
				rows.get(selected).collapse();
				selected = null;
				SelectionChangeEvent.fire(this);
			} else {
				// key is not selected and was not selected
			}
		} else {
			if (key == null) {
				rows.get(selected).collapse();
				selected = null;
				SelectionChangeEvent.fire(this);
			} else if (key.equals(selected)) {
				// was selected and is selected
			} else {
				// selected changed
				if (selected != null) {
					rows.get(selected).collapse();
				}
				rows.get(key).expand();
				selected = key;
				SelectionChangeEvent.fire(this);
			}
		}
	}

	public RowWidget getSelectedRowWidget() {
		return selected == null ? null : rows.get(selected);
	}

	public HandlerRegistration addSelectionChangeHandler(
			com.google.gwt.view.client.SelectionChangeEvent.Handler handler) {
		return addHandler(handler, SelectionChangeEvent.getType());
	}

	public void setProvider(InfiniteListDataProvider provider) {
		clear();
		this.provider = provider;
	}

	public InfiniteListDataProvider getProvider() {
		return provider;
	}

	public boolean isEndReached() {
		return endReached;
	}

	public void setEndReached(boolean endReached) {
		this.endReached = endReached;
	}

	public HandlerRegistration addNewDataHandler(NewDataHandler handler) {
		return addHandler(handler, NewDataEvent.getType());
	}
}
