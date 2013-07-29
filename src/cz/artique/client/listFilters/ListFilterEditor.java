package cz.artique.client.listFilters;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.shortcut.Shortcut;

/**
 * Editor shown inside {@link ListFilterDialog} and {@link AdhocDialog}; it
 * defines its layout and control.
 * 
 * @author Adam Juraszek
 * 
 */
public class ListFilterEditor extends Composite implements HasValue<ListFilter> {

	private static ListFilterEditorUiBinder uiBinder = GWT
		.create(ListFilterEditorUiBinder.class);

	interface ListFilterEditorUiBinder
			extends UiBinder<Widget, ListFilterEditor> {}

	@UiField
	Grid grid;

	@UiField
	TextBox name;

	@UiField
	TextBox hierarchy;

	@UiField
	TextBox exported;

	@UiField
	QueryFilter filter;

	@UiField
	DateBox startFrom;

	@UiField
	DateBox endTo;

	@UiField
	ReadStatePicker readPicker;

	@UiField
	ListFilterOrderPicker orderPicker;

	@UiField
	TextBox shortcut;

	@UiField
	Anchor RSS;

	@UiField
	Anchor Atom;

	@UiField
	InlineLabel notShared;

	private Key listFilterKey;
	private Key filterKey;

	/**
	 * Proper editor contains persistent fields (name, hierarchy, export alias
	 * and shortcut).
	 * 
	 * @param proper
	 *            whether the editor shows persistent {@link ListFilter}
	 */
	public ListFilterEditor(boolean proper) {
		initWidget(uiBinder.createAndBindUi(this));
		Element element = grid.getCellFormatter().getElement(4, 0);
		DOM.setElementAttribute(element, "colspan", "2");
		setProper(proper);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ListFilter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Currently specified {@link ListFilter}.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	public ListFilter getValue() {
		ListFilter lf = new ListFilter();
		String _name = name.getValue().trim();
		if (_name.equals(""))
			_name = null;
		lf.setName(_name);
		String _hierarchy = hierarchy.getValue().trim();
		if (!_hierarchy.startsWith("/"))
			_hierarchy = "/" + _hierarchy;
		lf.setHierarchy(_hierarchy);
		String _exported = exported.getValue();
		if (_exported != null)
			_exported.trim();
		if (_exported != null && _exported.equals(""))
			_exported = null;
		lf.setExportAlias(_exported);

		lf.setFilterObject(filter.getFilter());
		lf.setStartFrom(startFrom.getValue());
		lf.setEndTo(endTo.getValue());
		lf.setRead(readPicker.getValue() == null ? null : readPicker
			.getValue()
			.getState());
		lf.setOrder(orderPicker.getValue());

		lf.setKey(listFilterKey);
		lf.setFilter(filterKey);

		lf.setShortcutStroke(shortcut.getValue());
		return lf;
	}

	/**
	 * Sets field values to specified {@link ListFilter}.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue(ListFilter value) {
		name.setValue(value.getName());
		hierarchy.setValue(value.getHierarchy());
		exported.setValue(value.getExportAlias());
		setShared(value.getExportAlias());

		filter.setFilter(value.getFilterObject());
		startFrom.setValue(value.getStartFrom());
		endTo.setValue(value.getEndTo());

		Boolean read = value.getRead();
		readPicker.setValue(ReadState.get(read));
		orderPicker.setValue(value.getOrder());

		Shortcut cut =
			Managers.SHORTCUTS_MANAGER.getByReferenced(value.getKey());
		value.setShortcutStroke(cut != null ? cut.getKeyStroke() : null);
		shortcut.setValue(value.getShortcutStroke());

		listFilterKey = value.getKey();
		filterKey = value.getFilter();
	}

	/**
	 * Shows shared links iff the {@link ListFilter} is exported.
	 * 
	 * @param exportAlias
	 *            current export alias
	 */
	private void setShared(String exportAlias) {
		if (exportAlias != null)
			exportAlias.trim();
		if (exportAlias != null && exportAlias.equals(""))
			exportAlias = null;

		if (exportAlias != null) {
			String user =
				URL.encodeQueryString(ArtiqueWorld.WORLD
					.getUserInfo()
					.getNickname());
			String export = URL.encodeQueryString(exportAlias);
			String href =
				"/export/feedService?user=" + user + "&export=" + export;
			RSS.setVisible(true);
			RSS.setHref(href + "&type=rss");
			Atom.setVisible(true);
			Atom.setHref(href + "&type=atom");

			notShared.setVisible(false);
		} else {
			RSS.setVisible(false);
			Atom.setVisible(false);
			notShared.setVisible(true);
		}
	}

	public void setValue(ListFilter value, boolean fireEvents) {
		setValue(value);
	}

	/**
	 * Hides persistent-related rows for {@link AdhocDialog}.
	 * 
	 * @param proper
	 *            whether the editor shows persistent {@link ListFilter}
	 */
	private void setProper(boolean proper) {
		for (int i = 0; i < 4; i++) {
			Element element = grid.getRowFormatter().getElement(i);
			if (proper) {
				element.getStyle().clearDisplay();
			} else {
				element.getStyle().setDisplay(Display.NONE);
			}
		}
		{
			Element element =
				grid.getRowFormatter().getElement(grid.getRowCount() - 1);
			if (proper) {
				element.getStyle().clearDisplay();
			} else {
				element.getStyle().setDisplay(Display.NONE);
			}
		}
	}

}
