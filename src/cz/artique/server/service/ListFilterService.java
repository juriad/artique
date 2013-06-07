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

public class ListFilterService {

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

	public void fillFilters(ListFilter... listFilters) {
		fillFilters(Arrays.asList(listFilters));
	}

	public List<ListFilter> getAllListFilters(String userId) {
		ListFilterMeta meta = ListFilterMeta.get();
		List<ListFilter> listFilters =
			Datastore.query(meta).filter(meta.userId.equal(userId)).asList();
		fillFilters(listFilters);
		return listFilters;
	}

	public void createListFilter(ListFilter listFilter) {
		Filter filter = listFilter.getFilterObject();
		Key filterKey = saveFilter(filter);
		listFilter.setFilter(filterKey);
		Key key = Datastore.put(listFilter);
		listFilter.setKey(key);
	}

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

	public void deleteListFilter(ListFilter listFilter) {
		fillFilters(listFilter);
		deleteFilter(listFilter.getFilterObject());
		Datastore.delete(listFilter.getKey());
	}

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

	public ListFilter getListFilterByKey(Key key) {
		ListFilter listFilter = Datastore.get(ListFilterMeta.get(), key);
		fillFilters(listFilter);
		return listFilter;
	}
}
