package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.artique.client.listing.ScrollEndEvent.ScrollEndType;

public class AbstractInfiniteList<E> extends Composite
		implements InfiniteList<E>, HasScrollEndHandlers {

	private List<E> head = new ArrayList<E>();
	private List<E> body = new ArrayList<E>();
	private List<E> tail = new ArrayList<E>();
	private boolean exactCount;

	private final ScrollPanel scrollPanel;
	private final CellList<E> list;
	private final InfiniteListInfo info;

	public AbstractInfiniteList(InfiniteListCell<E> cell, InfiniteListInfo info) {
		this.info = info;
		// TODO resources

		SingleSelectionModel<E> selectionModel = new SingleSelectionModel<E>();
		list = new CellList<E>(cell);
		cell.setModelAndList(selectionModel, list);
		list.setSelectionModel(selectionModel);

		scrollPanel = new ScrollPanel();
		scrollPanel.addScrollHandler(new ScrollHandler() {
			public void onScroll(ScrollEvent event) {
				int pos = scrollPanel.getVerticalScrollPosition();
				if (pos == scrollPanel.getMinimumVerticalScrollPosition()) {
					fireEvent(new ScrollEndEvent(ScrollEndType.TOP));
				} else if (pos == scrollPanel
					.getMaximumVerticalScrollPosition()) {
					fireEvent(new ScrollEndEvent(ScrollEndType.BOTTOM));
				}
			}
		});
		initWidget(scrollPanel);
		scrollPanel.add(list);
	}

	public HandlerRegistration addRangeChangeHandler(
			final com.google.gwt.view.client.RangeChangeEvent.Handler handler) {
		return addHandler(handler, RangeChangeEvent.getType());
	}

	public HandlerRegistration addRowCountChangeHandler(
			final com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
		return addHandler(handler, RowCountChangeEvent.getType());
	}

	public int getRowCount() {
		return body.size();
	}

	public Range getVisibleRange() {
		return new Range(0, getRowCount());
	}

	public boolean isRowCountExact() {
		return exactCount;
	}

	public void setRowCount(int count) {
		setRowCount(count, true);
	}

	public void setRowCount(int count, boolean isExact) {
		// ignore count
		exactCount = isExact;
	}

	public void setVisibleRange(int start, int length) {
		setVisibleRange(new Range(start, length));
	}

	public void setVisibleRange(Range range) {
		// ignore
	}

	public void appendValues(List<E> values) {
		tail.addAll(values);
	}

	public void prependValues(List<E> values) {
		head.addAll(0, values);
	}

	public void setValue(E value) {
		for (int i = 0; i < body.size(); i++) {
			E v = body.get(i);
			if (v.equals(value)) {
				body.set(i, value);
				List<E> data = new ArrayList<E>();
				data.add(value);
				list.setRowData(i, data);
			}
		}
	}

	public int showHead() {
		List<E> l = head;
		head = new ArrayList<E>();
		body.addAll(0, l);
		list.setRowData(body);
		list.setRowCount(getRowCount(), isRowCountExact());
		RangeChangeEvent.fire(this, new Range(0, body.size()));
		RowCountChangeEvent.fire(this, body.size(), exactCount);
		return l.size();
	}

	public int showTail() {
		List<E> l = tail;
		tail = new ArrayList<E>();
		int index = body.size();
		body.addAll(l);
		list.setRowData(index, l);
		list.setRowCount(getRowCount(), isRowCountExact());
		RangeChangeEvent.fire(this, new Range(0, body.size()));
		RowCountChangeEvent.fire(this, body.size(), exactCount);
		return l.size();
	}

	public void clear() {
		list.setRowData(new ArrayList<E>());
		list.setRowCount(0, true);
	}

	public void refresh() {
		clear();
		appendValues(body);
	}

	public HandlerRegistration addScrollEndHandler(ScrollEndHandler handler) {
		return addHandler(handler, ScrollEndEvent.getType());
	}

	public void setRowCountExact(boolean rowCountExact) {
		this.exactCount = rowCountExact;
	}
}
