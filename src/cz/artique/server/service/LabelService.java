package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.label.FilterMeta;
import cz.artique.server.meta.label.LabelMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;

public class LabelService {

	public LabelService() {}

	public List<Label> getAllLabels(User user) {
		LabelMeta meta = LabelMeta.get();
		List<Label> labels =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();
		return labels;
	}

	public Label creatIfNotExist(Label label) {
		Key key = ServerUtils.genKey(label);
		LabelMeta meta = LabelMeta.get();
		Label theLabel = Datastore.getOrNull(meta, key);
		if (theLabel == null) {
			label.setKey(key);
			Datastore.put(label);
			theLabel = label;
		}
		return theLabel;
	}

	public List<Filter> getAllFilters(User user) {
		FilterMeta meta = FilterMeta.get();
		List<Filter> filters =
			Datastore
				.query(meta)
				.filter(meta.user.equal(user))
				.filter(meta.type.equal(FilterType.TOP_LEVEL_FILTER))
				.asList();
		for (Filter filter : filters) {
			List<Filter> subFilters = Datastore.get(meta, filter.getFilters());
			filter.setFilterObjects(subFilters);
		}
		return filters;
	}

	public Filter createFilter(Filter filter) {
		List<Key> subKeys = Datastore.put(filter.getFilterObjects());
		filter.setFilters(subKeys);
		Key key = Datastore.put(filter);
		filter.setKey(key);
		return filter;
	}

	public void updateFilter(Filter filter) {
		Datastore.put(filter);
	}

	public Filter getFilterByKey(Key key) {
		FilterMeta meta = FilterMeta.get();
		return Datastore.getOrNull(meta, key);
	}
}
