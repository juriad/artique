package cz.artique.client.artique;

import cz.artique.client.listing.InfiniteListInfo;

public class ArtiqueListInfo implements InfiniteListInfo {
	private int rowCount;
	private int headRowCount;

	public void setRowCount(int count) {
		this.rowCount = count;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setHeadRowCount(int count) {
		this.headRowCount = count;
	}

	public int getHeadRowCount() {
		return headRowCount;
	}
}
