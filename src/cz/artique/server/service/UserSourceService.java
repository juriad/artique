package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.server.meta.source.PageChangeCrawlerDataMeta;
import cz.artique.server.meta.source.RegionMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeCrawlerData;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;

public class UserSourceService {

	public UserSourceService() {}

	public void fillRegions(Iterable<UserSource> userSources) {
		Map<Key, List<UserSource>> map = new HashMap<Key, List<UserSource>>();
		for (UserSource us : userSources) {
			if (us.getRegion() != null) {
				if (!map.containsKey(us.getRegion())) {
					map.put(us.getRegion(), new ArrayList<UserSource>());
				}
				map.get(us.getRegion()).add(us);
			}
		}
		List<Region> regions = Datastore.get(RegionMeta.get(), map.keySet());
		for (Region region : regions) {
			List<UserSource> list = map.get(region.getKey());
			if (list != null) {
				for (UserSource us : list) {
					us.setRegionObject(region);
				}
			}
		}
	}

	public void fillRegions(UserSource... userSources) {
		fillRegions(Arrays.asList(userSources));
	}

	public void fillSources(Iterable<UserSource> userSources) {
		Map<Key, List<UserSource>> map = new HashMap<Key, List<UserSource>>();
		for (UserSource us : userSources) {
			if (us.getSource() != null) {
				if (!map.containsKey(us.getSource())) {
					map.put(us.getSource(), new ArrayList<UserSource>());
				}
				map.get(us.getSource()).add(us);
			}
		}
		List<Source> sources = Datastore.get(SourceMeta.get(), map.keySet());
		for (Source source : sources) {
			List<UserSource> list = map.get(source.getKey());
			if (list != null) {
				for (UserSource us : list) {
					us.setSourceObject(source);
				}
			}
		}
	}

	public void fillSources(UserSource... userSources) {
		fillSources(Arrays.asList(userSources));
	}

	/**
	 * Returns old if already exists
	 * 
	 * @param userSource
	 * @return incomplete usersource
	 */
	public UserSource createUserSource(UserSource userSource) {
		UserSource old = createUserSourceIfNotExist(userSource);
		if (old != null) {
			userSource = old;
		} else {
			Label l = createLabelForUserSource(userSource);
			userSource.setLabel(l.getKey());
			userSource.setLabelObject(l);
			List<Key> defaultLabels = userSource.getDefaultLabels();
			if (defaultLabels == null) {
				defaultLabels = new ArrayList<Key>();
			}
			defaultLabels.add(l.getKey());
			userSource.setDefaultLabels(defaultLabels);

			handleRegionChange(userSource);

			Datastore.put(userSource);
		}

		updateSourceUsage(null, userSource);
		return userSource;
	}

	private void handleRegionChange(UserSource userSource) {
		if (userSource.getRegion() == null
			&& userSource.getRegionObject() != null) {
			SourceService ss = new SourceService();
			ss.addRegion(userSource.getRegionObject());
			userSource.setRegion(userSource.getRegionObject().getKey());
		}
	}

	private UserSource createUserSourceIfNotExist(UserSource userSource) {
		Transaction tx = Datastore.beginTransaction();
		UserSource theUserSource = null;
		try {
			Key key = KeyGen.genKey(userSource);
			theUserSource = Datastore.getOrNull(tx, UserSourceMeta.get(), key);
			if (theUserSource == null) {
				userSource.setKey(key);
				Datastore.put(tx, userSource);
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return theUserSource;
	}

	private Label createLabelForUserSource(UserSource userSource) {
		Key labelKey;
		String labelName = KeyFactory.keyToString(userSource.getSource());
		final Label l = new Label(userSource.getUserId(), labelName);
		l.setLabelType(LabelType.USER_SOURCE);
		labelKey = KeyGen.genKey(l);
		l.setKey(labelKey);
		Datastore.put(l);
		return l;
	}

	public void updateUserSource(UserSource userSource) {
		handleRegionChange(userSource);
		Transaction tx = Datastore.beginTransaction();
		UserSource original;
		try {
			original =
				Datastore.get(tx, UserSourceMeta.get(), userSource.getKey());
			fillRegions(original);

			if (userSource.getCrawlerData() != null) {
				if (original.getRegionObject() == null
					|| !original.getRegionObject().equalsDeeply(
						userSource.getRegionObject())) {
					userSource.setCrawlerData(null);
				}
			}

			Datastore.put(tx, userSource);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		updateSourceUsage(original, userSource);
	}

	private void updateSourceUsage(UserSource original, UserSource userSource) {
		int diff = 0;
		if (original == null) {
			// new usersource
			if (userSource.isWatching()) {
				diff = 1;
			} else {
				// new but not watching, do nothing
			}
		} else {
			// updating usersource
			if (userSource.isWatching() && !original.isWatching()) {
				// start watching
				diff = 1;
			} else if (!userSource.isWatching() && original.isWatching()) {
				// stop watching
				diff = -1;
			}
		}

		if (diff == 0) {
			return;
		}

		Transaction tx = Datastore.beginTransaction();
		try {
			Source source =
				Datastore.get(tx, SourceMeta.get(), userSource.getSource());
			// extra for Manual Source, disables crawling
			if (source instanceof ManualSource) {
				source.setUsage(0);
				source.setEnabled(false);
			} else {
				source.setUsage(source.getUsage() + diff);
				source.setEnabled(source.getUsage() > 0);
			}
			Datastore.put(tx, source);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public List<UserSource> getUserSources(String userId) {
		UserSourceMeta meta = UserSourceMeta.get();
		ModelQuery<UserSource> query =
			Datastore.query(meta).filter(meta.userId.equal(userId));
		List<UserSource> userSources = query.asList();
		fillSources(userSources);
		fillRegions(userSources);
		return userSources;
	}

	public UserSource getManualUserSource(String userId) {
		SourceService ss = new SourceService();
		ManualSource manualSource = ss.ensureManualSource(userId);

		UserSource us = new UserSource(userId, manualSource, "does_not_matter");
		us.setKey(KeyGen.genKey(us));

		UserSource userSource =
			Datastore.getOrNull(UserSourceMeta.get(), us.getKey());
		if (userSource == null) {
			String name =
				ConfigService.CONFIG_SERVICE.getConfig(
					ServerConfigKey.MANUAL_SOURCE_NAME).<String> get();
			us.setName(name);
			us.setWatching(true);
			us.setSourceType(SourceType.MANUAL);
			userSource = createUserSource(us);
		}
		userSource.setSourceObject(manualSource);
		// will never have region
		return userSource;
	}

	public UserSource getUserSource(Key key) {
		UserSource userSource = Datastore.get(UserSourceMeta.get(), key);
		fillRegions(userSource);
		fillSources(userSource);
		return userSource;
	}

	public PageChangeCrawlerData getPageChangeCrawlerData(Key crawlerData) {
		PageChangeCrawlerData pageChangeCrawlerData =
			Datastore.get(PageChangeCrawlerDataMeta.get(), crawlerData);
		return pageChangeCrawlerData;
	}

	public void saveCrawlerData(PageChangeCrawlerData data) {
		Key key = Datastore.put(data);
		data.setKey(key);
	}

	public void setCrawlerData(List<UserSource> firstTime, Key crawlerDataKey) {
		List<Key> keys = new ArrayList<Key>();
		for (UserSource us : firstTime) {
			keys.add(us.getKey());
		}
		Transaction tx = Datastore.beginTransaction();
		try {
			List<UserSource> list =
				Datastore.get(tx, UserSourceMeta.get(), keys);
			for (UserSource us : list) {
				us.setCrawlerData(crawlerDataKey);
			}
			Datastore.put(tx, list);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public List<UserSource> getAllUserSources() {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore
				.query(meta)
				.filterInMemory(meta.sourceType.notEqual(SourceType.MANUAL))
				.sort(meta.userId.asc)
				.asList();
		return userSources;
	}
}
