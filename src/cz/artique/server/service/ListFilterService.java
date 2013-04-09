package cz.artique.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.label.FilterMeta;
import cz.artique.server.meta.label.ListFilterMeta;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.SharedUtils;

public class ListFilterService {

	public List<ListFilter> getAllListFilters(User user) {
		ListFilterMeta meta = ListFilterMeta.get();
		List<ListFilter> listFilters =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();

		Map<Key, ListFilter> map = new HashMap<Key, ListFilter>();
		for (ListFilter listFilter : listFilters) {
			map.put(listFilter.getFilter(), listFilter);
		}
		List<Filter> topLevel = Datastore.get(FilterMeta.get(), map.keySet());

		Map<Key, Filter> map2 = new HashMap<Key, Filter>();
		for (Filter top : topLevel) {
			if (top.getFilters() != null && !top.getFilters().isEmpty()) {
				for (Key k : top.getFilters()) {
					map2.put(k, top);
				}
			}
			map.get(top.getKey()).setFilterObject(top);
		}
		List<Filter> secondLevel =
			Datastore.get(FilterMeta.get(), map2.keySet());
		for (Filter second : secondLevel) {
			Filter top = map2.get(second.getKey());
			if (top.getFilterObjects() == null) {
				top.setFilterObjects(new ArrayList<Filter>());
			}
			top.getFilterObjects().add(second);
		}

		return listFilters;
	}

	public ListFilter createListFilter(ListFilter listFilter) {
		Filter filter = listFilter.getFilterObject();
		Key filterKey = saveFilter(filter);
		listFilter.setFilter(filterKey);
		Key key = Datastore.put(listFilter);
		listFilter.setKey(key);
		return listFilter;
	}

	public void updateListFilter(ListFilter listFilter) {
		Filter f = getFilter(listFilter.getFilter());
		if (!SharedUtils.deepEq(f, listFilter.getFilterObject())) {
			deleteFilter(f);
			Key filterKey = saveFilter(listFilter.getFilterObject());
			listFilter.setFilter(filterKey);
		}
		Datastore.put(listFilter);
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

	private Filter getFilter(Key key) {
		if (key == null) {
			return null;
		}
		Filter filter = Datastore.get(FilterMeta.get(), key);
		if (filter.getFilterObjects() != null
			&& !filter.getFilterObjects().isEmpty()) {
			List<Filter> subFilters =
				Datastore.get(FilterMeta.get(), filter.getFilters());
			filter.setFilterObjects(subFilters);
		}

		return filter;
	}

	private void deleteFilter(Filter filter) {
		if (filter != null) {
			List<Key> keys = filter.getFilters();
			if (keys == null) {
				keys = new ArrayList<Key>();
			}
			keys.add(filter.getKey());
			Datastore.deleteAsync(keys);
		}
	}

	public void deleteListFilter(ListFilter listFilter) {
		Filter f = getFilter(listFilter.getFilter());
		deleteFilter(f);
		Datastore.delete(listFilter.getKey());
	}
}
