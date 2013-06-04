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

import cz.artique.client.listing.row.RowWidget;
import cz.artique.client.listing.row.UserItemRow;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;

public class InfiniteList extends Composite
		implements HasSelectionChangedHandlers, HasScrollEndHandlers,
		HasNewDataHandlers {

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
		flowPanel.setStylePrimaryName("infiniteList");
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
		row.addOpenHandler(new OpenCloseHander());
		row.addCloseHandler(new OpenCloseHander());
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

	public RowWidget getSelectedRowWidget() {
		return selected == null ? null : rows.get(selected);
	}

	protected boolean setSelectedIndex(int index, boolean forceExpand) {
		try {
			RowWidget w = (RowWidget) flowPanel.getWidget(index);
			setSelectedKey(w.getKey(), forceExpand);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected int getSelectedIndex() {
		return flowPanel.getWidgetIndex(getSelectedRowWidget());
	}

	protected RowWidget getRow(int index) {
		return (RowWidget) flowPanel.getWidget(index);
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
