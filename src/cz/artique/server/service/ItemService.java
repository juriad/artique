package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.CompositeCriterion;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.InMemorySortCriterion;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.shared.list.ListingUpdate;
import cz.artique.shared.list.ListingUpdateRequest;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterOrder;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.UserSource;

public class ItemService {
	public ListingUpdate<UserItem> getItems(User user,
			ListingUpdateRequest request) {
		UserItemMeta meta = UserItemMeta.get();
		Date date = new Date();

		ListFilter listFilter;
		if (request.getListFilter() != null) {
			listFilter = request.getListFilter();
		} else {
			listFilter = new ListFilter();
		}

		FilterCriterion fc =
			getCriterionForFilter(listFilter != null ? listFilter
				.getFilterObject() : null);

		Key firstCut;
		if (listFilter.getStartFrom() != null) {
			firstCut = getLastBefore(listFilter.getStartFrom());
		} else {
			firstCut = Datastore.createKey(meta, -1);
		}

		Key firstHave;
		if (request.getFirstKey() != null) {
			firstHave = request.getFirstKey();
		} else {
			firstHave = Datastore.createKey(meta, Long.MAX_VALUE);
		}

		Key lastCut;
		if (listFilter.getEndTo() != null) {
			lastCut = getFirstAfter(listFilter.getEndTo());
		} else {
			lastCut = Datastore.createKey(meta, Long.MAX_VALUE);
		}

		Key lastHave;
		if (request.getLastKey() != null) {
			lastHave = request.getLastKey();
		} else {
			lastHave = Datastore.createKey(meta, -1);
		}

		boolean endReached;
		List<UserItem> head;
		List<UserItem> tail;

		if (FilterOrder.ASCENDING.equals(listFilter.getOrder())) {
			// head
			head = new ArrayList<UserItem>();

			// tail
			if (request.getFetchCount() > 0) {
				ModelQuery<UserItem> ascQuery =
					getBaseQuery(listFilter, fc, user);
				ascQuery =
					ascQuery.filter(
						meta.key.greaterThan(max(firstCut, lastHave))).filter(
						meta.key.lessThan(lastCut));

				tail =
					ascQuery
						.sort(meta.key.asc)
						.limit(request.getFetchCount())
						.asList();
				List<Key> tailKeys = getListOfItemKeys(tail);
				fetchItemsForUserItems(tail, tailKeys);
				if (tail.size() < request.getFetchCount()) {
					endReached = true;
				} else {
					endReached = false;
				}
			} else {
				tail = new ArrayList<UserItem>();
				endReached = false;
			}
		} else {
			// head
			if (listFilter.getEndTo() == null) {
				if (request.getLastKey() != null) {
					ModelQuery<UserItem> headQuery =
						getBaseQuery(listFilter, fc, user);
					headQuery =
						headQuery.filter(meta.key.greaterThan(lastHave));
					head = headQuery.sort(meta.key.desc).asList();
					List<Key> headKeys = getListOfItemKeys(head);
					fetchItemsForUserItems(head, headKeys);
				} else {
					head = new ArrayList<UserItem>();
				}
			} else {
				head = new ArrayList<UserItem>();
			}

			// tail
			if (request.getFetchCount() > 0) {
				ModelQuery<UserItem> tailQuery =
					getBaseQuery(listFilter, fc, user);
				tailQuery =
					tailQuery.filter(meta.key.greaterThan(firstCut)).filter(
						meta.key.lessThan(min(firstHave, lastCut)));
				tail =
					tailQuery
						.sort(meta.key.desc)
						.limit(request.getFetchCount())
						.asList();
				List<Key> tailKeys = getListOfItemKeys(tail);
				fetchItemsForUserItems(tail, tailKeys);
				if (tail.size() < request.getFetchCount()) {
					endReached = true;
				} else {
					endReached = false;
				}
			} else {
				tail = new ArrayList<UserItem>();
				endReached = false;
			}
		}

		List<UserItem> modified;
		if (firstHave.compareTo(lastHave) <= 0) {
			ModelQuery<UserItem> modifiedQuery =
				getBaseQuery(listFilter, fc, user);
			modifiedQuery =
				modifiedQuery.filter(meta.lastChanged
					.greaterThanOrEqual(request.getLastFetch()));
			modifiedQuery =
				modifiedQuery.filterInMemory(
					meta.key.greaterThanOrEqual(firstHave)).filterInMemory(
					meta.key.lessThanOrEqual(lastHave));
			InMemorySortCriterion sortCriterion;
			if (FilterOrder.ASCENDING.equals(listFilter.getOrder())) {
				sortCriterion = meta.key.asc;
			} else {
				sortCriterion = meta.key.desc;
			}
			modified = modifiedQuery.sortInMemory(sortCriterion).asList();
		} else {
			modified = new ArrayList<UserItem>();
		}

		return new ListingUpdate<UserItem>(head, modified, tail, date,
			endReached);
	}

	private ModelQuery<UserItem> getBaseQuery(ListFilter listFilter,
			FilterCriterion fc, User user) {
		UserItemMeta meta = UserItemMeta.get();

		ModelQuery<UserItem> query =
			Datastore.query(meta).filter(meta.user.equal(user));
		if (fc != null) {
			query = query.filter(fc);
		}
		if (listFilter.getRead() != null) {
			query = query.filter(meta.read.equal(listFilter.getRead()));
		}

		return query;
	}

	private Key getLastBefore(Date cut) {
		UserItemMeta meta = UserItemMeta.get();
		List<Key> asKeyList =
			Datastore
				.query(meta)
				.filter(meta.added.lessThan(cut))
				.sort(meta.added.desc)
				.limit(1)
				.asKeyList();
		if (asKeyList.size() > 0) {
			return asKeyList.get(0);
		} else {
			return Datastore.createKey(meta, -1);
		}
	}

	private Key getFirstAfter(Date cut) {
		UserItemMeta meta = UserItemMeta.get();
		List<Key> asKeyList =
			Datastore
				.query(meta)
				.filter(meta.added.greaterThan(cut))
				.sort(meta.added.asc)
				.limit(1)
				.asKeyList();
		if (asKeyList.size() > 0) {
			return asKeyList.get(0);
		} else {
			return Datastore.createKey(meta, Long.MAX_VALUE);
		}
	}

	public static Key min(Key... ks) {
		if (ks == null || ks.length == 0) {
			return null;
		}

		Key min = ks[0];
		for (int i = 1; i < ks.length; i++) {
			if (ks[i].compareTo(min) < 0) {
				min = ks[i];
			}
		}
		return min;
	}

	public static Key max(Key... ks) {
		if (ks == null || ks.length == 0) {
			return null;
		}

		Key max = ks[0];
		for (int i = 1; i < ks.length; i++) {
			if (ks[i].compareTo(max) > 0) {
				max = ks[i];
			}
		}
		return max;
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
		UserItemMeta meta = UserItemMeta.get();
		Transaction transaction = Datastore.beginTransaction();
		UserItem userItem = Datastore.get(transaction, meta, item.getKey());
		userItem.setLabels(item.getLabels());
		userItem.setRead(item.isRead());
		userItem.setLastChanged(new Date());
		Datastore.put(transaction, userItem);
		transaction.commit();
	}
}
