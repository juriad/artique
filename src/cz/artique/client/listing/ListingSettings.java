package cz.artique.client.listing;

import cz.artique.shared.model.label.ListFilter;

public class ListingSettings {
	private final ListFilter listFilter;
	private final int initSize;
	private final int step;
	private final int interval;

	public ListingSettings(ListFilter listFilter, int initSize, int step,
			int interval) {
		super();
		this.listFilter = listFilter;
		this.initSize = initSize;
		this.step = step;
		this.interval = interval;
	}

	public ListFilter getListFilter() {
		return listFilter;
	}

	public int getInitSize() {
		return initSize;
	}

	public int getStep() {
		return step;
	}

	public int getInterval() {
		return interval;
	}
}
