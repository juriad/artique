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
import cz.artique.shared.model.source.CrawlerData;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeCrawlerData;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;

/**
 * Provides methods which manipulates with entity {@link UserSource} in
 * database.
 * It also contains several other methods which are related to
 * {@link UserSource}s and nearby entities.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserSourceService {

	public UserSourceService() {}

	/**
	 * Takes userSources, extracts keys of {@link Region}s, fetches them from
	 * database
	 * and sets regionObjects of all {@link UserSource}s.
	 * 
	 * @param userSources
	 *            iterable of {@link UserSource}s
	 */
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

	/**
	 * Calls {@link #fillRegions(Iterable)}; this is a wrapper used for a single
	 * {@link UserSource}
	 * 
	 * @param userSources
	 *            list of {@link UserSource}s, usually a single one
	 */
	public void fillRegions(UserSource... userSources) {
		fillRegions(Arrays.asList(userSources));
	}

	/**
	 * Takes userSources, extracts keys of {@link Source}s, fetches them from
	 * database
	 * and sets sourceObjects of all {@link UserSource}s.
	 * 
	 * @param userSources
	 *            iterable of {@link UserSource}s
	 */
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

	/**
	 * Calls {@link #fillSources(Iterable)}; this is a wrapper used for a single
	 * {@link UserSource}
	 * 
	 * @param userSources
	 *            list of {@link UserSource}s, usually a single one
	 */
	public void fillSources(UserSource... userSources) {
		fillSources(Arrays.asList(userSources));
	}

	/**
	 * Creates a new {@link UserSource} if such does not exist yet.
	 * Returns old if already exists.
	 * Calls {@link #createUserSourceIfNotExist(UserSource)} and than handles
	 * the rest: labels, regions.
	 * 
	 * @param userSource
	 *            prototype of {@link UserSource}
	 * @return new {@link UserSource} or existing one
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

	/**
	 * Detects change of {@link Region}; it that case, it creates a new
	 * {@link Region} and sets
	 * its key to {@link UserSource}.
	 * 
	 * @param userSource
	 */
	private void handleRegionChange(UserSource userSource) {
		if (userSource.getRegion() == null
			&& userSource.getRegionObject() != null) {
			SourceService ss = new SourceService();
			ss.addRegion(userSource.getRegionObject());
			userSource.setRegion(userSource.getRegionObject().getKey());
		}
	}

	/**
	 * Creates a new {@link UserSource} if such does not exist yet.
	 * Returns old if already exists
	 * 
	 * @param userSource
	 *            prototype of {@link UserSource}
	 * @return incomplete {@link UserSource} or existing one
	 */
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

	/**
	 * Creates a new {@link Label} for newly-created {@link UserSource}.
	 * The {@link Label} will be of type {@link LabelType#USER_SOURCE}.
	 * 
	 * @param userSource
	 *            {@link UserSource} the {@link Label} is being created for
	 * @return new {@link Label}
	 */
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

	/**
	 * Updates existing {@link UserSource}.
	 * 
	 * @param userSource
	 *            {@link UserSource} to update
	 */
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

	/**
	 * Updates usage of underlying {@link Source}.
	 * Adds +1 if started to be watched, -1 if stopped watching.
	 * 
	 * @param original
	 *            former {@link UserSource} before change
	 * @param userSource
	 *            {@link UserSource} after change
	 */
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

	/**
	 * Gets all {@link UserSource}s for a single user.
	 * 
	 * @param userId
	 *            the user the {@link UserSource}s are gotten for
	 * @return list of all {@link UserSource}s
	 */
	public List<UserSource> getUserSources(String userId) {
		UserSourceMeta meta = UserSourceMeta.get();
		ModelQuery<UserSource> query =
			Datastore.query(meta).filter(meta.userId.equal(userId));
		List<UserSource> userSources = query.asList();
		fillSources(userSources);
		fillRegions(userSources);
		return userSources;
	}

	/**
	 * Gets {@link UserSource} of {@link ManualSource} for a user.
	 * 
	 * @param userId
	 *            the user the {@link UserSource} of {@link ManualSource} is
	 *            gotten for
	 * @return {@link UserSource} of {@link ManualSource}
	 */
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

	/**
	 * Gets whole {@link UserSource} by its key.
	 * 
	 * @param key
	 *            key of {@link UserSource}
	 * @return {@link UserSource}
	 */
	public UserSource getUserSource(Key key) {
		UserSource userSource = Datastore.get(UserSourceMeta.get(), key);
		fillRegions(userSource);
		fillSources(userSource);
		return userSource;
	}

	/**
	 * Gets {@link PageChangeCrawlerData} by its key.
	 * 
	 * @param crawlerData
	 *            key of {@link PageChangeCrawlerData}
	 * @return {@link PageChangeCrawlerData}
	 */
	public PageChangeCrawlerData getPageChangeCrawlerData(Key crawlerData) {
		PageChangeCrawlerData pageChangeCrawlerData =
			Datastore.get(PageChangeCrawlerDataMeta.get(), crawlerData);
		return pageChangeCrawlerData;
	}

	/**
	 * @param data
	 *            {@link PageChangeCrawlerData} to save
	 */
	public void saveCrawlerData(PageChangeCrawlerData data) {
		Key key = Datastore.put(data);
		data.setKey(key);
	}

	/**
	 * Sets key of {@link CrawlerData} to all {@link UserSource}s in list.
	 * 
	 * @param firstTime
	 *            list of {@link UserSource}s
	 * @param crawlerDataKey
	 *            key of {@link CrawlerData} to be set
	 */
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
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	/**
	 * @return all existing {@link UserSource}s
	 */
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

	/**
	 * Gets list of active {@link UserSource}s for {@link Source} identified by
	 * its key.
	 * 
	 * @param sourceKey
	 *            key of {@link Source}
	 * @return list of active {@link UserSource}s
	 */
	public List<UserSource> getActiveUserSourcesForSource(Key sourceKey) {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore
				.query(meta)
				.filter(meta.source.equal(sourceKey))
				.filter(meta.watching.equal(Boolean.TRUE))
				.asList();
		UserSourceService uss = new UserSourceService();
		uss.fillRegions(userSources);
		return userSources;
	}
}
