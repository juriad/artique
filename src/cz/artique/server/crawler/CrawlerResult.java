package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.List;

import cz.artique.shared.model.item.Item;

public class CrawlerResult {
	private List<Item> items;
	private List<Throwable> errors;

	public CrawlerResult() {
		this.items = new ArrayList<Item>();
		this.errors = new ArrayList<Throwable>();
	}

	public void addError(Throwable t) {
		errors.add(t);
	}

	public void addItem(Item i) {
		items.add(i);
	}

	public List<Item> getItems() {
		return items;
	}

	public List<Throwable> getErrors() {
		return errors;
	}

	public boolean isError() {
		return !errors.isEmpty();
	}

	public int getItemsCount() {
		return items.size();
	}

}
