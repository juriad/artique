package cz.artique.client.artiqueHistory;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import cz.artique.shared.model.label.ListFilter;

public class ArtiqueHistoryHandler implements ValueChangeHandler<String> {

	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println("fired history change");
		HistoryItem lastHistoryItem =
			ArtiqueHistory.HISTORY.getLastHistoryItem();
		ListFilter toShow = null;
		if (lastHistoryItem == null) {
			toShow = HistoryUtils.UTILS.deserializeListFilter(event.getValue());
		} else {
			HistoryItem byToken =
				ArtiqueHistory.HISTORY.getByToken(event.getValue());

			if (byToken == null) {
				toShow =
					HistoryUtils.UTILS.deserializeListFilter(event.getValue());
			} else {
				toShow = byToken.getListFilter();
			}
		}
		ArtiqueHistory.HISTORY.addListFilter(toShow, event.getValue(), false);
		CachingHistoryUtils.UTILS.setBaseListFilter();
	}
}
