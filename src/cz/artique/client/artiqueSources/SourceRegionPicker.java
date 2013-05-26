package cz.artique.client.artiqueSources;

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
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
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

			ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();

			sb.appendHtmlConstant("<table><tr>");
			sb.appendHtmlConstant("<td style='font-weight:bold;'>");
			if (value.getName() == null) {
				sb
					.appendHtmlConstant("<i>" + constants.customRegion()
						+ "</i>");
			} else {
				sb.appendEscaped(value.getName());
			}
			sb.appendHtmlConstant("</td></tr><tr><td style='color: green;'>");
			sb.appendEscaped(value.getPositiveSelector() == null ? "" : value
				.getPositiveSelector());
			sb.appendHtmlConstant("</td></tr><tr><td style='color: red;'>");
			sb.appendEscaped(value.getNegativeSelector() == null ? "" : value
				.getNegativeSelector());
			sb.appendHtmlConstant("</td></tr></table>");
		}
	}

	@UiField(provided = true)
	CellList<Region> cellList;

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
		cellList = new CellList<Region>(new RegionCell());
		final SingleSelectionModel<Region> selectionModel =
			new SingleSelectionModel<Region>();
		cellList.setSelectionModel(selectionModel);
		selectionModel
			.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					selectionChanged(selectionModel.getSelectedObject());
				}
			});

		initWidget(uiBinder.createAndBindUi(this));
	}

	protected void selectionChanged(Region selected) {
		name.setEnabled(selected.getKey() == null);
		name.setValue(selected.getName());
		positive.setEnabled(selected.getKey() == null);
		positive.setText(selected.getPositiveSelector());
		negative.setEnabled(selected.getKey() == null);
		negative.setText(selected.getNegativeSelector());
		checkButton.setEnabled(selected.getKey() == null);
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		String headerText =
			selected.getName() == null ? constants.customRegion() : selected
				.getName();
		header.setText(headerText);
	}

	@UiHandler("checkButton")
	protected void checkButtonClicked(ClickEvent event) {
		@SuppressWarnings("unchecked")
		SingleSelectionModel<Region> selectionModel =
			(SingleSelectionModel<Region>) cellList.getSelectionModel();
		Region selectedObject = selectionModel.getSelectedObject();
		if (!new Region().equals(selectedObject)) {
			// not custom or null
			return;
		}
		Managers.SOURCES_MANAGER.checkRegion(selectedObject,
			new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					notPassedValidation();
				}

				public void onSuccess(Boolean result) {
					passedValidation();
				}
			});
	}

	private void notPassedValidation() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
			constants.regionCheckError()));
	}

	private void passedValidation() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.INFO,
			constants.regionCheckPass()));
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public UserSource getValue() {
		@SuppressWarnings("unchecked")
		SingleSelectionModel<Region> selectionModel =
			(SingleSelectionModel<Region>) cellList.getSelectionModel();
		Region selectedObject = selectionModel.getSelectedObject();
		if (cellList.getRowCount() == 0 || selectedObject == null) {
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

	private void failedToLoadList() {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		Managers.MESSAGES_MANAGER.addMessage(new Message(MessageType.ERROR,
			constants.failedToGetRegions()));
	}

	public void setValue(final UserSource value) {
		this.userSource = value;
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
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
						failedToLoadList();
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
						cellList.getSelectionModel().setSelected(regionObject,
							true);
					}
				});
		}
	}

	public void setValue(UserSource value, boolean fireEvents) {
		setValue(value);
	}
}
