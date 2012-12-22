package cz.artique.client;

import cz.artique.shared.model.label.Filter;

public class ListingSettings {
	private final Filter filter;
	private final int initSize;
	private final Boolean read;
	private final int step;
	private final int interval;
	private final int timeout;

	public ListingSettings(Filter filter, int initSize, Boolean read, int step,
			int interval, int timeout) {
		super();
		this.filter = filter;
		this.initSize = initSize;
		this.read = read;
		this.step = step;
		this.interval = interval;
		this.timeout = timeout;
	}

	public Filter getFilter() {
		return filter;
	}

	public int getInitSize() {
		return initSize;
	}

	public Boolean getRead() {
		return read;
	}

	public int getStep() {
		return step;
	}

	public int getInterval() {
		return interval;
	}

	public int getTimeout() {
		return timeout;
	}

}
