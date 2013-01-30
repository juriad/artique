package cz.artique.client.artiqueHistory;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueListing.ArtiqueListProvider;
import cz.artique.shared.model.label.ListFilter;

public class HistoryHandler implements ValueChangeHandler<String> {

	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println("fired history change");
		String lastToken = ArtiqueHistory.HISTORY.getLastToken();
		ListFilter deserialized = null;
		if (lastToken == null) {
			deserialized = HistoryUtils.deserializeListFilter(event.getValue());
			ArtiqueHistory.HISTORY.addListFilter(deserialized,
				event.getValue(), false);
		}
		ListFilter toShow = ArtiqueHistory.HISTORY.getByToken(event.getValue());
		boolean existsInHistory = toShow != null;
		if (!existsInHistory) {
			if (deserialized != null) {
				toShow = deserialized;
			} else {
				toShow = HistoryUtils.deserializeListFilter(event.getValue());
			}
			ArtiqueHistory.HISTORY.addListFilter(toShow, event.getValue(),
				false);
		}

		ArtiqueListProvider provider =
			new ArtiqueListProvider(toShow, ArtiqueWorld.WORLD.getList());
	}
}
