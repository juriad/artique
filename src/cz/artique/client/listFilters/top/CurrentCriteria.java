package cz.artique.client.listFilters.top;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.i18n.I18n;
import cz.artique.client.listFilters.ListFiltersConstants;
import cz.artique.client.listFilters.ReadState;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;

/**
 * Shows human readable currently applied criteria of {@link ListFilter} except
 * {@link Label} query.
 * 
 * @author Adam Juraszek
 * 
 */
public class CurrentCriteria extends Composite {
	private static CurrentCriteriaUiBinder uiBinder = GWT
		.create(CurrentCriteriaUiBinder.class);

	interface CurrentCriteriaUiBinder extends UiBinder<Widget, CurrentCriteria> {}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("CurrentCriteria.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	@UiField
	InlineLabel readState;
	@UiField
	InlineLabel items;

	@UiField
	InlineLabel fromLabel;
	@UiField
	DateLabel from;

	@UiField
	InlineLabel toLabel;
	@UiField
	DateLabel to;

	@UiField
	InlineLabel orderedLabel;
	@UiField
	InlineLabel ordered;

	public CurrentCriteria() {
		res.style().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		setStylePrimaryName("currentCriteria");
	}

	/**
	 * Sets a new {@link ListFilter} to be shown.
	 * 
	 * @param listFilter
	 *            new {@link ListFilter}
	 */
	public void setListFilter(ListFilter listFilter) {
		if (listFilter == null) {
			listFilter = new ListFilter();
		}

		ListFiltersConstants constants = I18n.getListFiltersConstants();

		ReadState read = ReadState.get(listFilter.getRead());
		readState.setText(constants.getString("readState_" + read.name()));

		Date fromDate = listFilter.getStartFrom();
		if (fromDate != null) {
			fromLabel.setVisible(true);
			from.setVisible(true);
			from.setValue(fromDate);
		} else {
			fromLabel.setVisible(false);
			from.setVisible(false);
		}

		Date toDate = listFilter.getEndTo();
		if (toDate != null) {
			toLabel.setVisible(true);
			to.setVisible(true);
			to.setValue(toDate);
		} else {
			toLabel.setVisible(false);
			to.setVisible(false);
		}

		ListFilterOrder order = listFilter.getOrder();
		if (order == null) {
			order = ListFilterOrder.getDefault();
		}

		ordered.setText(constants.getString("listFilterOrder_" + order.name()));
	}
}
