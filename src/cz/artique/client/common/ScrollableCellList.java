package cz.artique.client.common;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.gwt.view.client.SingleSelectionModel;

public class ScrollableCellList<T> extends Composite
		implements HasSelectionChangedHandlers {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("ScrollableCellList.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private final ScrollPanel scroll;

	private final CellList<T> cellList;

	private final SingleSelectionModel<T> selectionModel;

	public ScrollableCellList(final Cell<T> cell) {
		res.style().ensureInjected();
		ScrollableCellListResource.INSTANCE.cellListStyle().ensureInjected();
		scroll = new ScrollPanel();
		initWidget(scroll);
		setStylePrimaryName("scrollableCellList");

		cellList = new CellList<T>(cell, ScrollableCellListResource.INSTANCE);
		cellList.setStylePrimaryName("cellList");
		scroll.add(cellList);

		selectionModel = new SingleSelectionModel<T>();
		cellList.setSelectionModel(selectionModel);
	}

	@UiChild(tagname = "emptyListWidget", limit = 1)
	public void addEmptyListWidget(Widget widget) {
		cellList.setEmptyListWidget(widget);
		if (widget != null) {
			widget.addStyleName("emptyList");
		}
	}

	public HandlerRegistration addSelectionChangeHandler(Handler handler) {
		return selectionModel.addSelectionChangeHandler(handler);
	}

	public T getSelected() {
		return selectionModel.getSelectedObject();
	}

	public void setSelected(T object, boolean selected) {
		selectionModel.setSelected(object, selected);
	}

	public void clearSelection() {
		selectionModel.clear();
	}

	public void setRowData(List<? extends T> values) {
		cellList.setRowData(values);
	}

	public boolean isEmpty() {
		return cellList.getRowCount() == 0;
	}

	static interface ScrollableCellListResource extends CellList.Resources {

		public static ScrollableCellListResource INSTANCE = GWT
			.create(ScrollableCellListResource.class);

		/**
		 * The styles used in this widget.
		 */
		@Source(ScrollableCellListStyle.DEFAULT_CSS)
		Style cellListStyle();
	}

	static interface ScrollableCellListStyle extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "ModifiedCellList.css";

		/**
		 * Applied to even items.
		 */
		String cellListEvenItem();

		/**
		 * Applied to the keyboard selected item.
		 */
		String cellListKeyboardSelectedItem();

		/**
		 * Applied to odd items.
		 */
		String cellListOddItem();

		/**
		 * Applied to selected items.
		 */
		String cellListSelectedItem();

		/**
		 * Applied to the widget.
		 */
		String cellListWidget();
	}
}
