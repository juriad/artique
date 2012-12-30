package cz.artique.client.artique;

import java.util.ArrayList;
import java.util.List;

import cz.artique.client.listing.ListingSettings;
import cz.artique.shared.model.source.UserSource;

public enum ListingHistory {
	HISTORY;

	private List<ListingSettings> historySettings;
	private int cursor;

	private ListingHistory() {
		clear();
	}

	public void addHistory(ListingSettings settings) {
		historySettings.subList(Math.min(historySettings.size(), cursor + 1),
			historySettings.size()).clear();
		historySettings.add(settings);
		cursor++;
	}

	public void clear() {
		historySettings = new ArrayList<ListingSettings>();
		cursor = -1;
	}

	public ListingSettings current() {
		if (cursor >= 0) {
			return historySettings.get(cursor);
		}
		return null;
	}

	public ListingSettings back() {
		if (cursor >= 0) {
			cursor--;
		}
		if (cursor >= 0) {
			return historySettings.get(cursor);
		}
		return null;
	}

	public ListingSettings forward() {
		if (cursor < historySettings.size() - 1) {
			cursor++;
		}
		if (cursor >= 0) {
			return historySettings.get(cursor);
		}
		return null;
	}

	public void showSource(UserSource object) {
		// TODO Auto-generated method stub
		System.out.println("show source " + object);
	}
}
