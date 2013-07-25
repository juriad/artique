package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.label.FilterMeta;
import cz.artique.server.meta.label.ListFilterMeta;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.SharedUtils;

/**
 * Provides methods which manipulates with entity {@link ListFilter} in
 * database.
 * It also contains several other methods which are related to
 * {@link ListFilter}s.
 * 
 * @author Adam Juraszek
 * 
 */
public class ListFilterService {

	/**
	 * Takes listFilters, extracts keys of {@link Filter}s, fetches them from
	 * database
	 * and sets filterObjects of all {@link ListFilter}s.
	 * 
	 * @param listFilters
	 *            iterable of {@link ListFilter}s
	 */
	public void fillFilters(Iterable<ListFilter> listFilters) {
		Map<Key, List<ListFilter>> map = new HashMap<Key, List<ListFilter>>();
		for (ListFilter lf : listFilters) {
			if (lf.getFilter() != null) {
				if (!map.containsKey(lf.getFilter())) {
					map.put(lf.getFilter(), new ArrayList<ListFilter>());
				}
				map.get(lf.getFilter()).add(lf);
			}
		}
		List<Filter> filters = Datastore.get(FilterMeta.get(), map.keySet());
		for (Filter filter : filters) {
			List<ListFilter> list = map.get(filter.getKey());
			if (list != null) {
				if (filter.getFilters() != null
					&& filter.getFilters().size() > 0) {
					// second level:
					List<Filter> list2 =
						Datastore.get(FilterMeta.get(), filter.getFilters());
					filter.setFilterObjects(list2);
				}

				for (ListFilter lf : list) {
					lf.setFilterObject(filter);
				}
			}
		}
	}

	/**
	 * Calls {@link #fillFilters(Iterable)}; this is a wrapper used for a single
	 * {@link ListFilter}
	 * 
	 * @param listFilters
	 *            list of {@link ListFilter}s, usually a single one
	 */
	public void fillFilters(ListFilter... listFilters) {
		fillFilters(Arrays.asList(listFilters));
	}

	/**
	 * Gets all {@link ListFilter}s for a single user.
	 * 
	 * @param userId
	 *            the user the {@link ListFilter}s are gotten for
	 * @return list of all {@link ListFilter}s
	 */
	public List<ListFilter> getAllListFilters(String userId) {
		ListFilterMeta meta = ListFilterMeta.get();
		List<ListFilter> listFilters =
			Datastore.query(meta).filter(meta.userId.equal(userId)).asList();
		fillFilters(listFilters);
		return listFilters;
	}

	/**
	 * Creates a new {@link ListFilter} in database.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be created
	 */
	public void createListFilter(ListFilter listFilter) {
		Filter filter = listFilter.getFilterObject();
		Key filterKey = saveFilter(filter);
		listFilter.setFilter(filterKey);
		Key key = Datastore.put(listFilter);
		listFilter.setKey(key);
	}

	/**
	 * Updates an existing {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be updated
	 */
	public void updateListFilter(ListFilter listFilter) {
		ListFilter old = new ListFilter();
		old.setFilter(listFilter.getFilter());
		fillFilters(old);

		boolean toDelete = false;
		if (!SharedUtils.deepEq(old.getFilterObject(),
			listFilter.getFilterObject())) {
			toDelete = true;
			Key filterKey = saveFilter(listFilter.getFilterObject());
			listFilter.setFilter(filterKey);
		}
		Datastore.put(listFilter);
		if (toDelete) {
			deleteFilter(old.getFilterObject());
		}
	}

	/**
	 * Saves {@link Filter} to database.
	 * A new {@link Filter} is created in database when filterObject of
	 * {@link ListFilter} is changed in any way.
	 * The old one is deleted.
	 * 
	 * @see #deleteFilter(Filter)
	 * @param filter
	 *            {@link Filter} to be saved
	 * @return key of saved {@link Filter}
	 */
	private Key saveFilter(Filter filter) {
		if (filter == null) {
			return null;
		}
		if (filter.getFilterObjects() != null
			&& !filter.getFilterObjects().isEmpty()) {
			List<Key> subKeys = Datastore.put(filter.getFilterObjects());
			filter.setFilters(subKeys);
			for (int i = 0; i < subKeys.size(); i++) {
				filter.getFilterObjects().get(i).setKey(subKeys.get(i));
			}
		}
		Key filterKey = Datastore.put(filter);
		filter.setKey(filterKey);
		return filter.getKey();
	}

	/**
	 * Deletes {@link Filter} from database.
	 * 
	 * @param filter
	 *            {@link Filter} to be deleted
	 */
	private void deleteFilter(Filter filter) {
		if (filter == null) {
			return;
		}
		List<Key> keys = filter.getFilters();
		if (keys == null) {
			keys = new ArrayList<Key>();
		}
		keys.add(filter.getKey());
		Datastore.deleteAsync(keys);
	}

	/**
	 * Deletes {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be deleted
	 */
	public void deleteListFilter(ListFilter listFilter) {
		fillFilters(listFilter);
		deleteFilter(listFilter.getFilterObject());
		Datastore.delete(listFilter.getKey());
	}

	/**
	 * Gets {@link ListFilter} of a user identified by its export alias.
	 * 
	 * @param alias
	 *            export alias of the {@link ListFilter}
	 * @param userId
	 *            owner of the {@link ListFilter}
	 * @return found {@link ListFilter} or null if it does not exist
	 */
	public ListFilter getExportByAlias(String alias, String userId) {
		ListFilterMeta meta = ListFilterMeta.get();
		List<ListFilter> exports =
			Datastore
				.query(meta)
				.filter(meta.exportAlias.equal(alias))
				.filter(meta.userId.equal(userId))
				.asList();
		if (exports.isEmpty()) {
			return null;
		} else {
			fillFilters(exports);
			return exports.get(0);
		}
	}

	/**
	 * Gets {@link ListFilter} by its key.
	 * 
	 * @param key
	 *            key of {@link ListFilter}
	 * @return {@link ListFilter}
	 */
	public ListFilter getListFilterByKey(Key key) {
		ListFilter listFilter = Datastore.get(ListFilterMeta.get(), key);
		fillFilters(listFilter);
		return listFilter;
	}
}
