package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slim3.datastore.CompositeCriterion;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.server.meta.item.ArticleItemMeta;
import cz.artique.server.meta.item.ItemMeta;
import cz.artique.server.meta.item.LinkItemMeta;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.shared.items.ChangeSet;
import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.LinkItem;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

/**
 * Provides methods which manipulates with entity {@link UserItem} and
 * {@link Item} in database.
 * It also contains several other methods which are related to these entities.
 * 
 * @author Adam Juraszek
 * 
 */
public class ItemService {
	/**
	 * Takes userItems, extracts keys of {@link Item}s, fetches them from
	 * database
	 * and sets itemObject of all {@link UserItem}s.
	 * 
	 * @param userItems
	 *            iterable of {@link UserItem}s
	 */
	public void fillItems(Iterable<UserItem> userItems) {
		Map<Key, List<UserItem>> map = new HashMap<Key, List<UserItem>>();
		for (UserItem ui : userItems) {
			if (ui.getItem() != null) {
				if (!map.containsKey(ui.getItem())) {
					map.put(ui.getItem(), new ArrayList<UserItem>());
				}
				map.get(ui.getItem()).add(ui);
			}
		}
		List<Item> items = Datastore.get(ItemMeta.get(), map.keySet());
		for (Item item : items) {
			List<UserItem> list = map.get(item.getKey());
			if (list != null) {
				for (UserItem ui : list) {
					ui.setItemObject(item);
				}
			}
		}
	}

	/**
	 * Calls {@link #fillItems(Iterable)}; this is a wrapper used for a single
	 * {@link UserItem}
	 * 
	 * @param userItems
	 *            list of {@link UserItem}s, usually a single one
	 */
	public void fillItems(UserItem... userItems) {
		fillItems(Arrays.asList(userItems));
	}

	/**
	 * The main method used when requesting {@link UserItem}s shown in infinite
	 * list.
	 * 
	 * @param userId
	 *            the user the items are gotten for
	 * @param request
	 *            criteria of desired items
	 * @return response containing list of matching items
	 */
	public ListingResponse getItems(String userId, ListingRequest request) {
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

		if (ListFilterOrder.ASCENDING.equals(listFilter.getOrder())) {
			// head
			head = new ArrayList<UserItem>();

			// tail
			if (request.getFetchCount() > 0) {
				ModelQuery<UserItem> ascQuery =
					getBaseQuery(listFilter, fc, userId);
				ascQuery =
					ascQuery.filter(
						meta.key.greaterThan(max(firstCut, lastHave))).filter(
						meta.key.lessThan(lastCut));

				tail =
					ascQuery
						.sort(meta.key.asc)
						.limit(request.getFetchCount())
						.asList();
				fillItems(tail);
				fillSerializedKeys(tail);
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
						getBaseQuery(listFilter, fc, userId);
					headQuery =
						headQuery.filter(meta.key.greaterThan(lastHave));
					head = headQuery.sort(meta.key.desc).asList();
					fillItems(head);
					fillSerializedKeys(head);
				} else {
					head = new ArrayList<UserItem>();
				}
			} else {
				head = new ArrayList<UserItem>();
			}

			// tail
			if (request.getFetchCount() > 0) {
				ModelQuery<UserItem> tailQuery =
					getBaseQuery(listFilter, fc, userId);
				tailQuery =
					tailQuery.filter(meta.key.greaterThan(firstCut)).filter(
						meta.key.lessThan(min(firstHave, lastCut)));
				tail =
					tailQuery
						.sort(meta.key.desc)
						.limit(request.getFetchCount())
						.asList();
				fillItems(tail);
				fillSerializedKeys(tail);
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
		return new ListingResponse(head, tail, date, endReached);
	}

	/**
	 * Constructs query filtered by user and read state (optionally).
	 * 
	 * @param listFilter
	 *            {@link ListFilter}
	 * @param fc
	 *            another criterion
	 * @param userId
	 *            owner of the {@link UserItem}s
	 * @return query prototype
	 */
	private ModelQuery<UserItem> getBaseQuery(ListFilter listFilter,
			FilterCriterion fc, String userId) {
		UserItemMeta meta = UserItemMeta.get();

		ModelQuery<UserItem> query =
			Datastore.query(meta).filter(meta.userId.equal(userId));
		if (fc != null) {
			query = query.filter(fc);
		}
		if (listFilter.getRead() != null) {
			query = query.filter(meta.read.equal(listFilter.getRead()));
		}

		return query;
	}

	/**
	 * Returns the last {@link UserItem} before specified date.
	 * 
	 * @param cut
	 *            date
	 * @return such {@link UserItem} or key of value -1
	 */
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

	/**
	 * Returns the first {@link UserItem} after specified date.
	 * 
	 * @param cut
	 *            date
	 * @return such {@link UserItem} or key of value infinity
	 */
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

	/**
	 * Returns minimum of several keys.
	 * 
	 * @param ks
	 *            list of keys
	 * @return minimal one
	 */
	private static Key min(Key... ks) {
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

	/**
	 * Returns maximum of several keys.
	 * 
	 * @param ks
	 *            list of keys
	 * @return maximal one
	 */
	private static Key max(Key... ks) {
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

	/**
	 * Constructs complex criterion for {@link Filter}.
	 * 
	 * @param filter
	 *            {@link Filter}
	 * @return criterion
	 */
	private FilterCriterion getCriterionForFilter(Filter filter) {
		if (filter == null) {
			return null;
		}
		UserItemMeta meta = UserItemMeta.get();
		List<FilterCriterion> ors = new ArrayList<FilterCriterion>();
		if (filter.getLabels() != null) {
			for (Key label : filter.getLabels()) {
				ors.add(meta.labels.equal(label));
			}
		}
		if (filter.getFilterObjects() != null) {
			for (Filter sub : filter.getFilterObjects()) {
				List<FilterCriterion> ands = new ArrayList<FilterCriterion>();
				for (Key label : sub.getLabels()) {
					ands.add(meta.labels.equal(label));
				}
				if (ands.size() > 1) {
					FilterCriterion and =
						new CompositeCriterion(meta,
							CompositeFilterOperator.AND,
							ands.toArray(new FilterCriterion[ands.size()]));
					ors.add(and);
				} else {
					ors.addAll(ands);
				}
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

	/**
	 * Gets {@link UserItem} by its key.
	 * 
	 * @param key
	 *            key of {@link UserItem}
	 * @return {@link UserItem}
	 */
	public UserItem getByKey(Key key) {
		UserItem userItem = Datastore.getOrNull(UserItemMeta.get(), key);
		if (userItem != null) {
			fillItems(userItem);
		}
		return userItem;
	}

	/**
	 * Adds a new {@link ManualItem} and corresponding {@link UserItem}.
	 * 
	 * @param userItem
	 *            {@link UserItem} of the new {@link ManualItem}
	 */
	public void addManualItem(UserItem userItem) {
		UserSourceService uss = new UserSourceService();
		UserSource manualUserSource =
			uss.getManualUserSource(userItem.getUserId());

		ManualItem item = (ManualItem) userItem.getItemObject();
		item.setSource(manualUserSource.getSource());
		Key itemKey = Datastore.put(item);
		item.setKey(itemKey);

		userItem.setItem(item.getKey());
		Set<Key> labels = new HashSet<Key>();
		if (userItem.getLabels() != null) {
			labels.addAll(userItem.getLabels());
		}
		labels.add(manualUserSource.getLabel());
		userItem.setLabels(new ArrayList<Key>(labels));
		userItem.setUserSource(manualUserSource.getKey());

		Key allocatedId = Datastore.allocateId(UserItemMeta.get());
		userItem.setKey(allocatedId);
		Datastore.put(userItem);
	}

	/**
	 * Updates list of {@link UserItem} based on set of changes.
	 * 
	 * @param userItems
	 *            {@link UserItem}s to change
	 * @param changeSets
	 *            set of changes for each {@link UserItem}
	 * @param userId
	 *            restriction to user
	 * @return updated {@link UserItem}s
	 */
	public Map<Key, UserItem> updateItems(List<UserItem> userItems,
			Map<Key, ChangeSet> changeSets, String userId) {
		Map<Key, UserItem> result = new HashMap<Key, UserItem>();
		for (UserItem userItem : userItems) {
			if (!userItem.getUserId().equals(userId)) {
				// error, ignore this item
				continue;
			}
			ChangeSet change = changeSets.get(userItem.getKey());
			Set<Key> labelsToAdd = change.getLabelsAdded();
			userItem.getLabels().addAll(labelsToAdd);
			Set<Key> labelsToRemove = change.getLabelsRemoved();
			userItem.getLabels().removeAll(labelsToRemove);
			if (change.getReadState() != null) {
				userItem.setRead(change.getReadState());
			}
			userItem.setLastChanged(new Date());
			result.put(userItem.getKey(), userItem);
		}
		Datastore.put(userItems);
		return result;
	}

	/**
	 * Sets key of backed up webpage to {@link UserItem} identified by its key.
	 * 
	 * @param userItemKey
	 *            key of {@link UserItem}
	 * @param blobKey
	 *            key of backed up webpage
	 */
	public void setBackup(Key userItemKey, boolean backup) {
		// TODO nice to have: delete old backup if current backupBlobKey != null
		Transaction tx = Datastore.beginTransaction();
		try {
			UserItem userItem =
				Datastore.get(tx, UserItemMeta.get(), userItemKey);
			userItem.setBackup(true);
			userItem.setLastChanged(new Date());
			Datastore.put(tx, userItem);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	/**
	 * Creates a new {@link Item} in database.
	 * 
	 * @param item
	 *            {@link Item} to be created
	 */
	public void addItem(Item item) {
		Key key = Datastore.put(item);
		item.setKey(key);
	}

	/**
	 * Gets list of existing {@link UserItem}s corresponding to an {@link Item}
	 * identified by its key.
	 * 
	 * @param itemKey
	 *            key of {@link Item}
	 * @return list of corresponding {@link UserItem}s
	 */
	public List<UserItem> getUserItemsForItem(Key itemKey) {
		UserItemMeta meta = UserItemMeta.get();
		List<UserItem> list =
			Datastore.query(meta).filter(meta.item.equal(itemKey)).asList();
		return list;
	}

	/**
	 * Returns list of incomplete {@link UserItem}s by their keys.
	 * 
	 * @param itemKeys
	 *            keys of desired {@link UserItem}s
	 * @return list of incomplete {@link UserItem}s
	 */
	public List<UserItem> getUserItemsByKeys(Iterable<Key> itemKeys) {
		List<UserItem> list = Datastore.get(UserItemMeta.get(), itemKeys);
		return list;
	}

	/**
	 * Gets list of {@link ArticleItem}s with the same hash and {@link Source}.
	 * 
	 * @param sourceKey
	 *            key of {@link Source}
	 * @param hash
	 *            hash of {@link Item}
	 * @return list of {@link ArticleItem}s
	 */
	public List<ArticleItem> getCollidingArticleItems(Key sourceKey, String hash) {
		ArticleItemMeta meta = ArticleItemMeta.get();
		List<ArticleItem> items =
			Datastore
				.query(meta)
				.filter(meta.source.equal(sourceKey))
				.filter(meta.hash.equal(hash))
				.asList();
		return items;
	}

	/**
	 * Saves list of new {@link UserItem}s to database.
	 * 
	 * @param userItems
	 *            list of {@link UserItem}s to save
	 */
	public void saveUserItems(List<UserItem> userItems) {
		KeyRange allocatedIds =
			Datastore.allocateIds(UserItemMeta.get(), userItems.size());
		int i = 0;
		for (Key key : allocatedIds) {
			UserItem ui = userItems.get(i);
			ui.setKey(key);
			i++;
		}
		Datastore.put(userItems);
	}

	/**
	 * Gets list of {@link LinkItem}s with the same hash and {@link Source}.
	 * 
	 * @param sourceKey
	 *            key of {@link Source}
	 * @param hash
	 *            hash of {@link Item}
	 * @return list of {@link LinkItem}s
	 */
	public List<LinkItem> getCollidingLinkItems(Key sourceKey, String hash) {
		LinkItemMeta meta = LinkItemMeta.get();
		List<LinkItem> items =
			Datastore
				.query(meta)
				.filter(meta.source.equal(sourceKey))
				.filter(meta.hash.equal(hash))
				.asList();
		return items;
	}

	public void fillSerializedKeys(List<UserItem> userItems) {
		for (UserItem ui : userItems) {
			ui.setSerializedKey(KeyFactory.keyToString(ui.getKey()));
		}
	}
}
