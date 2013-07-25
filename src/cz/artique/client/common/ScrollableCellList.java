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

/**
 * {@link CellList} inside {@link ScrollPanel} with altered style.
 * The {@link CellList} already has set {@link SingleSelectionModel}.
 * 
 * @author Adam Juraszek
 * 
 * @param <T>
 *            cell type
 */
public class ScrollableCellList<T> extends Composite
		implements HasSelectionChangedHandlers {

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

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("ScrollableCellList.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private final ScrollPanel scroll;

	private final CellList<T> cellList;

	private final SingleSelectionModel<T> selectionModel;

	/**
	 * @param cell
	 *            cell the {@link CellList} will contain
	 */
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

	/**
	 * Sets widget shown when the {@link CellList} is empty.
	 * 
	 * @param widget
	 */
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

	/**
	 * @return selected value
	 */
	public T getSelected() {
		return selectionModel.getSelectedObject();
	}

	/**
	 * Sets selected value.
	 * 
	 * @param object
	 *            new value
	 */
	public void setSelected(T object) {
		selectionModel.setSelected(object, true);
	}

	/**
	 * Clears selection.
	 */
	public void clearSelection() {
		selectionModel.clear();
	}

	/**
	 * Sets list of value shown in cells.
	 * 
	 * @param values
	 *            list of values
	 */
	public void setRowData(List<? extends T> values) {
		cellList.setRowData(values);
	}

	/**
	 * @return whether the {@link CellList} is empty
	 */
	public boolean isEmpty() {
		return cellList.getRowCount() == 0;
	}
}
