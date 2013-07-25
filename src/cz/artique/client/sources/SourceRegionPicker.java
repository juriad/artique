package cz.artique.client.sources;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

import cz.artique.client.common.ScrollableCellList;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.UserSource;

public class SourceRegionPicker extends Composite
		implements HasValue<UserSource> {
	class RegionCell extends AbstractCell<Region> {

		public RegionCell() {}

		@Override
		public void render(Context context, Region value, SafeHtmlBuilder sb) {
			// Value can be null, so do a null check..
			if (value == null) {
				return;
			}

			SourcesConstants constants = I18n.getSourcesConstants();

			if (value.getName() == null) {
				sb
					.appendHtmlConstant("<i>" + constants.customRegion()
						+ "</i>");
			} else {
				sb.appendEscaped(value.getName());
			}
		}
	}

	@UiField(provided = true)
	ScrollableCellList<Region> cellList;

	@UiField
	TextBox name;

	@UiField
	TextBox positive;

	@UiField
	TextBox negative;

	@UiField
	Button checkButton;

	@UiField
	Label header;

	@UiField
	DisclosurePanel disclosure;

	private static SourceRegionPickerUiBinder uiBinder = GWT
		.create(SourceRegionPickerUiBinder.class);

	interface SourceRegionPickerUiBinder
			extends UiBinder<Widget, SourceRegionPicker> {}

	private UserSource userSource;

	public SourceRegionPicker() {
		cellList = new ScrollableCellList<Region>(new RegionCell());
		initWidget(uiBinder.createAndBindUi(this));
		cellList.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				selectionChanged(cellList.getSelected());
			}
		});
	}

	protected void selectionChanged(Region selected) {
		name.setEnabled(selected.getKey() == null);
		name.setValue(selected.getName());
		positive.setEnabled(selected.getKey() == null);
		positive.setText(selected.getPositiveSelector());
		negative.setEnabled(selected.getKey() == null);
		negative.setText(selected.getNegativeSelector());
		checkButton.setEnabled(selected.getKey() == null);
		SourcesConstants constants = I18n.getSourcesConstants();
		String headerText =
			selected.getName() == null ? constants.customRegion() : selected
				.getName();
		header.setText("» " + headerText);
	}

	@UiHandler("checkButton")
	protected void checkButtonClicked(ClickEvent event) {
		Region selectedObject = cellList.getSelected();
		if (!new Region().equals(selectedObject)) {
			// not custom or null
			return;
		}

		Region regionObject = getValue().getRegionObject();
		Managers.SOURCES_MANAGER.checkRegion(regionObject, null);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public UserSource getValue() {
		Region selectedObject = cellList.getSelected();
		if (cellList.isEmpty() || selectedObject == null) {
			return userSource;
		} else {
			UserSource us = new UserSource();
			if (selectedObject.getKey() == null) {
				if (name.getText().trim().isEmpty()) {
					selectedObject = null;
				} else {
					selectedObject = new Region(userSource.getSource());
					selectedObject.setName(name.getText());
					selectedObject.setNegativeSelector(negative.getText());
					selectedObject.setPositiveSelector(positive.getText());
				}
			}

			us.setRegion(selectedObject != null
				? selectedObject.getKey()
				: null);
			us.setRegionObject(selectedObject);
			return us;
		}
	}

	public void setValue(final UserSource value) {
		this.userSource = value;
		SourcesConstants constants = I18n.getSourcesConstants();
		disclosure.setOpen(value.getKey() == null);

		if (value.getSource() == null) {
			// does not exist yet
			cellList.setRowData(new ArrayList<Region>());
			name.setText(constants.unavailable());
			name.setEnabled(false);
			positive.setText(constants.unavailable());
			positive.setEnabled(false);
			negative.setText(constants.unavailable());
			negative.setEnabled(false);
			checkButton.setEnabled(false);
			header.setText(constants.unavailable());
		} else {
			cellList.setRowData(new ArrayList<Region>());
			selectionChanged(new Region(userSource.getSource()));

			Managers.SOURCES_MANAGER.getRegions(userSource.getSource(),
				new AsyncCallback<List<Region>>() {
					public void onFailure(Throwable caught) {
						cellList.setRowData(new ArrayList<Region>());
						Region regionObject = userSource.getRegionObject();
						if (regionObject == null) {
							regionObject = new Region(userSource.getSource());
						}
						selectionChanged(regionObject);
					}

					public void onSuccess(List<Region> result) {
						Region custom = new Region(userSource.getSource());
						result.add(custom);
						cellList.setRowData(result);
						Region regionObject = userSource.getRegionObject();
						if (regionObject == null) {
							regionObject = custom;
						}
						selectionChanged(regionObject);
						cellList.setSelected(regionObject);
					}
				});
		}
	}

	public void setValue(UserSource value, boolean fireEvents) {
		setValue(value);
	}
}
