package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.CompositeCriterion;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.source.UserSource;

public class ItemService {
	public ListingUpdate<UserItem> getItems(User user,
			ListingUpdateRequest request) {
		UserItemMeta meta = UserItemMeta.get();

		Date date = new Date();

		FilterCriterion fc = getCriterionForFilter(request.getFilter());

		// head
		List<UserItem> addedSinceLast;
		if (request.getFirstKey() != null) {
			ModelQuery<UserItem> query =
				Datastore.query(meta).filter(meta.user.equal(user));
			if (fc != null) {
				query = query.filter(fc);
			}

			if (request.getRead() != null) {
				query = query.filter(meta.read.equal(request.getRead()));
			}

			addedSinceLast =
				query
					.filter(meta.key.greaterThan(request.getFirstKey()))
					.sort(meta.key.desc)
					.asList();

			List<Key> addedSinceLastKeys = getListOfItemKeys(addedSinceLast);
			fetchItemsForUserItems(addedSinceLast, addedSinceLastKeys);
		} else {
			addedSinceLast = new ArrayList<UserItem>();
		}

		// tail
		boolean endReached = false;
		List<UserItem> tail;
		if (request.getFetchCount() > 0) {
			Key lastKey = request.getLastKey();
			if (lastKey == null) {
				lastKey = Datastore.createKey(meta, Long.MAX_VALUE);
			}

			ModelQuery<UserItem> query =
				Datastore.query(meta).filter(meta.user.equal(user));
			if (fc != null) {
				query = query.filter(fc);
			}

			if (request.getRead() != null) {
				query = query.filter(meta.read.equal(request.getRead()));
			}

			tail =
				query
					.filter(meta.key.lessThan(lastKey))
					.sort(meta.key.desc)
					.limit(request.getFetchCount())
					.asList();

			List<Key> tailKeys = getListOfItemKeys(tail);
			fetchItemsForUserItems(tail, tailKeys);
			if (tailKeys.size() < request.getFetchCount()) {
				// not all items has been fetched
				// therefore, select reached the end
				endReached = true;
			}
		} else {
			tail = new ArrayList<UserItem>();
		}

		// updated
		List<UserItem> updatedSinceLast;
		if (request.getFirstKey() != null && request.getLastKey() != null) {
			ModelQuery<UserItem> query =
				Datastore.query(meta).filter(meta.user.equal(user));
			if (fc != null) {
				query = query.filter(fc);
			}

			if (request.getRead() != null) {
				query = query.filter(meta.read.equal(request.getRead()));
			}

			updatedSinceLast =
				query
					.filter(
						meta.lastChanged.greaterThanOrEqual(request
							.getLastFetch()))
					.filterInMemory(
						meta.key.greaterThanOrEqual(request.getLastKey()))
					.filterInMemory(
						meta.key.lessThanOrEqual(request.getFirstKey()))
					.sortInMemory(meta.key.desc)
					.asList();
		} else {
			updatedSinceLast = new ArrayList<UserItem>();
		}

		return new ListingUpdate<UserItem>(addedSinceLast, updatedSinceLast,
			tail, date, endReached);
	}

	private FilterCriterion getCriterionForFilter(Filter filter) {
		if (filter == null) {
			return null;
		}
		UserItemMeta meta = UserItemMeta.get();
		List<FilterCriterion> ors = new ArrayList<FilterCriterion>();
		for (Key label : filter.getLabels()) {
			ors.add(meta.labels.equal(label));
		}
		for (Filter sub : filter.getFilterObjects()) {
			List<FilterCriterion> ands = new ArrayList<FilterCriterion>();
			for (Key label : sub.getLabels()) {
				ands.add(meta.labels.equal(label));
			}
			if (ands.size() > 1) {
				FilterCriterion and =
					new CompositeCriterion(meta, CompositeFilterOperator.AND,
						ands.toArray(new FilterCriterion[ands.size()]));
				ors.add(and);
			} else {
				ors.addAll(ands);
			}
		}

		if (ors.size() > 1) {
			FilterCriterion or =
				new CompositeCriterion(meta, CompositeFilterOperator.OR,
					ors.toArray(new FilterCriterion[ors.size()]));
			return or;
		} else if (ors.size() == 1) {
			return ors.get(0);
		} else {
			return null;
		}
	}

	private List<Key> getListOfItemKeys(List<UserItem> userItems) {
		List<Key> keys = new ArrayList<Key>(userItems.size());
		for (UserItem ui : userItems) {
			keys.add(ui.getItem());
		}
		return keys;
	}

	private void fetchItemsForUserItems(List<UserItem> userItems, List<Key> keys) {
		ItemMeta iMeta = ItemMeta.get();
		List<Item> items = Datastore.get(iMeta, keys);
		for (int i = 0; i < items.size(); i++) {
			userItems.get(i).setItemObject(items.get(i));
		}
	}

	public UserItem addManualItem(ManualItem item) {
		UserSourceService uss = new UserSourceService();
		UserSource manualSource = uss.getManualSource();

		item.setSource(manualSource.getSource());
		Key key = Datastore.put(item);
		item.setKey(key);

		UserItem ui = new UserItem(item, manualSource);
		Datastore.put(ui);
		return ui;
	}

	public void updateUserItem(UserItem item) {
		item.setLastChanged(new Date());
		Datastore.put(item);
	}
}
