package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;

public class ArtiqueList<E> extends Composite implements InfiniteList<E> {

	class VerticalPanelWithReplace extends VerticalPanel {
		public void replace(int index, Widget newWidget) {
			remove(index);
			insert(newWidget, index);
		}
	}

	private List<E> head = new ArrayList<E>();
	private List<E> body = new ArrayList<E>();
	private List<E> tail = new ArrayList<E>();
	private boolean exactCount;

	private final VerticalPanelWithReplace panel;
	private final WidgetFactory<E> factory;
	private final ScrollPanel scrollPanel;

	public ArtiqueList(WidgetFactory<E> factory) {
		this.factory = factory;
		this.panel = new VerticalPanelWithReplace();
		scrollPanel = new ScrollPanel(panel);
		initWidget(scrollPanel);
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
				replaceWidget(i, value);
			}
		}
	}

	public int showHead() {
		List<E> l = head;
		head = new ArrayList<E>();
		body.addAll(0, l);
		prependWidgets(l);
		RangeChangeEvent.fire(this, new Range(0, body.size()));
		RowCountChangeEvent.fire(this, body.size(), exactCount);
		return l.size();
	}

	public int showTail() {
		List<E> l = tail;
		tail = new ArrayList<E>();
		body.addAll(l);
		appendWidgets(l);
		RangeChangeEvent.fire(this, new Range(0, body.size()));
		RowCountChangeEvent.fire(this, body.size(), exactCount);
		return l.size();
	}

	private void appendWidgets(List<E> l) {
		for (E e : l) {
			Widget w = factory.createWidget(e);
			panel.add(w);
		}
	}

	private void prependWidgets(List<E> l) {
		for (int i = l.size(); i >= 0; i--) {
			E e = l.get(i);
			Widget w = factory.createWidget(e);
			panel.insert(w, 0);
		}
	}

	private void replaceWidget(int i, E e) {
		Widget w = factory.createWidget(e);
		panel.replace(i, w);
	}

	public void clear() {
		panel.clear();
	}

	public void refresh() {
		clear();
		appendValues(body);
	}
}
