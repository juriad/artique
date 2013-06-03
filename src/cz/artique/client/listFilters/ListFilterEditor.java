package cz.artique.client.listFilters;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.common.OptionalValue;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.shortcut.Shortcut;

public class ListFilterEditor extends Composite
		implements HasEnabled, HasValue<ListFilter> {

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
	OptionalValue<TextBox, String> exported;

	@UiField
	QueryFilter filter;

	@UiField
	OptionalValue<DateBox, Date> startFrom;

	@UiField
	OptionalValue<DateBox, Date> endTo;

	@UiField
	OptionalValue<ReadStatePicker, ReadState> readPicker;

	@UiField
	ListFilterOrderPicker orderPicker;

	@UiField
	OptionalValue<TextBox, String> shortcut;

	private Key listFilterKey;
	private Key filterKey;

	private boolean enabled = true;

	public ListFilterEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		Element element = grid.getCellFormatter().getElement(3, 0);
		DOM.setElementAttribute(element, "colspan", "2");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		name.setEnabled(enabled);
		hierarchy.setEnabled(enabled);
		exported.setEnabled(enabled);
		filter.setEnabled(enabled);
		startFrom.setEnabled(enabled);
		endTo.setEnabled(enabled);
		readPicker.setEnabled(enabled);
		orderPicker.setEnabled(enabled);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ListFilter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

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
			.isState());
		lf.setOrder(orderPicker.getValue());

		lf.setUser(ArtiqueWorld.WORLD.getUser());
		lf.setKey(listFilterKey);
		lf.setFilter(filterKey);

		lf.setShortcutStroke(shortcut.getValue());
		return lf;
	}

	public void setValue(ListFilter value) {
		name.setValue(value.getName());
		hierarchy.setValue(value.getHierarchy());
		exported.setValue(value.getExportAlias());

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

	public void setValue(ListFilter value, boolean fireEvents) {
		setValue(value);
	}

	public void setProper(boolean proper) {
		for (int i = 0; i < 3; i++) {
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
