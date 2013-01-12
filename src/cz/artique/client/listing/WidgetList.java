package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.HasKeyProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

import cz.artique.shared.utils.HasKey;

public class WidgetList<E extends HasKey<K>, K> extends Composite
		implements InfiniteList<E>, HasKeyProvider<E>, ProvidesKey<E>,
		HasSelectionChangedHandlers, ExpandCollapseHandler {

	private final FlowPanel flowPanel;
	private Map<K, RowWidget<E, K>> rows;
	private K selected = null;

	private List<E> head;
	private List<E> tail;

	private boolean rowCountExact;
	private final RowWidgetFactory<E, K> factory;
	protected final ScrollPanel scrollPanel;
	private InfiniteListDataProvider<E> provider;

	public WidgetList(RowWidgetFactory<E, K> factory) {
		this.factory = factory;
		flowPanel = new FlowPanel();
		rows = new HashMap<K, RowWidget<E, K>>();
		head = new ArrayList<E>();
		tail = new ArrayList<E>();

		scrollPanel = new ScrollPanel();
		initWidget(scrollPanel);
		scrollPanel.add(flowPanel);
	}

	public HandlerRegistration addRangeChangeHandler(Handler handler) {
		throw new UnsupportedOperationException();
	}

	public HandlerRegistration addRowCountChangeHandler(
			com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
		return addHandler(handler, RowCountChangeEvent.getType());
	}

	public int getRowCount() {
		return rows.size();
	}

	public Range getVisibleRange() {
		return new Range(0, rows.size());
	}

	public boolean isRowCountExact() {
		return rowCountExact;
	}

	public void setRowCount(int count) {
		setRowCount(count, true);
	}

	public void setRowCount(int count, boolean isExact) {
		throw new UnsupportedOperationException();
	}

	public void setVisibleRange(int start, int length) {
		setVisibleRange(new Range(start, length));
	}

	public void setVisibleRange(Range range) {
		throw new UnsupportedOperationException();
	}

	public HandlerRegistration addScrollEndHandler(ScrollEndHandler handler) {
		return addHandler(handler, ScrollEndEvent.getType());
	}

	public void appendValues(List<E> values) {
		tail.addAll(values);
	}

	public void prependValues(List<E> values) {
		head.addAll(0, values);
	}

	public void setValue(E value) {
		RowWidget<E, K> row = rows.get(value.getKey());
		if (row != null) {
			row.setNewData(value);
		}
	}

	public int showHead() {
		List<E> l = head;
		if (l.size() > 0) {
			head = new ArrayList<E>();

			for (E e : l) {
				RowWidget<E, K> row = createRow(e);
				flowPanel.insert(row, 0);
				rows.put(row.getKey(), row);
			}

			RowCountChangeEvent.fire(this, getRowCount(), isRowCountExact());
			fetchToFillPage();
		}
		return l.size();
	}

	public int showTail() {
		List<E> l = tail;
		if (l.size() > 0) {
			tail = new ArrayList<E>();

			for (E e : l) {
				RowWidget<E, K> row = createRow(e);
				flowPanel.add(row);
				rows.put(row.getKey(), row);
			}

			RowCountChangeEvent.fire(this, getRowCount(), isRowCountExact());
			fetchToFillPage();
		}
		return l.size();
	}

	protected void fetchToFillPage() {}

	private RowWidget<E, K> createRow(E e) {
		RowWidget<E, K> row = factory.createWidget(e);
		row.addExpandCollapseHandler(this);
		return row;
	}

	public void clear() {
		flowPanel.clear();
		rows.clear();
		selected = null;
	}

	public void setSelectedKey(K key, boolean isSelected) {
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

	public K getSelectedKey() {
		return selected;
	}

	public E getSelectedValue() {
		return selected == null ? null : getSelectedRowWidget().getData(false);
	}

	public RowWidget<E, K> getSelectedRowWidget() {
		return selected == null ? null : rows.get(getSelectedKey());
	}

	public ProvidesKey<E> getKeyProvider() {
		return this;
	}

	public K getKey(E item) {
		return item.getKey();
	}

	public HandlerRegistration addSelectionChangeHandler(
			com.google.gwt.view.client.SelectionChangeEvent.Handler handler) {
		return addHandler(handler, SelectionChangeEvent.getType());
	}

	public void setRowCountExact(boolean rowCountExact) {
		this.rowCountExact = rowCountExact;
	}

	public void onExpandOrCollapse(ExpandCollapseEvent e) {
		if (e.getSource() instanceof RowWidget) {
			@SuppressWarnings("unchecked")
			RowWidget<E, K> row = (RowWidget<E, K>) e.getSource();
			setSelectedKey(row.getKey(), row.isExpanded());
		}
	}

	public void setProvider(InfiniteListDataProvider<E> provider) {
		clear();
		this.provider = provider;
	}

	public InfiniteListDataProvider<E> getProvider() {
		return provider;
	}
}
