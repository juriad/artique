package cz.artique.client.artiqueListing;

import java.util.HashMap;
import java.util.Map;

import cz.artique.client.listing.ListingSettings;
import cz.artique.shared.model.source.UserSource;

public enum ListingHistory {
	HISTORY;

	private Map<Integer, ListingSettings> historySettings;

	private ListingHistory() {
		clear();
	}

	public void addHistory(ListingSettings settings) {
		historySettings.put(historySettings.size(), settings);
	}

	public void clear() {
		historySettings = new HashMap<Integer, ListingSettings>();
	}
	
	public int size() {
		return historySettings.size();
	}

	public ListingSettings getHistory(int index) {
		return historySettings.get(index);
	}

	public void showSource(UserSource object) {
		// TODO toto tady nema co delat
		System.out.println("show source " + object);
	}
}
