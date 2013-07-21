package cz.artique.client.listFilters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import cz.artique.client.common.UniversalDialog;
import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;

public class AdhocDialog extends UniversalDialog<ListFilter> {

	public static final AdhocDialog DIALOG = new AdhocDialog();

	public AdhocDialog() {
		ListFiltersConstants constants = I18n.getListFiltersConstants();
		setText(constants.adhocDialog());
		final ListFilterEditor editor = new ListFilterEditor(false);
		setWidget(editor);

		addButton(constants.wipeButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				editor.setValue(new ListFilter());
			}
		});

		addButton(constants.applyButton(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ListFilter value = editor.getValue();
				String token =
					CachingHistoryUtils.UTILS.serializeListFilter(value);
				Managers.HISTORY_MANAGER.setListFilter(value, token);
				hide();
			}
		});

		addButton(constants.closeButton(), HIDE);

		setShowAction(new OnShowAction<ListFilter>() {
			public void onShow(ListFilter param) {
				editor.setValue(param);
			}
		});
	}
}
