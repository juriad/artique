package cz.artique.server.service;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.RegionMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.source.CheckStat;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;

/**
 * Provides methods which manipulates with entity {@link Source} in
 * database.
 * It also contains several other methods which are related to {@link Source}s
 * and nearby entities.
 * 
 * @author Adam Juraszek
 * 
 */
public class SourceService {
	/**
	 * Gets list of existing {@link Region}s for a {@link Source} identified by
	 * its key.
	 * 
	 * @param source
	 *            key of {@link Source}
	 * @return list of existing {@link Region}s
	 */
	public List<Region> getAllRegions(Key source) {
		RegionMeta meta = RegionMeta.get();
		List<Region> list =
			Datastore
				.query(meta)
				.filter(meta.htmlSource.equal(source))
				.asList();
		return list;
	}

	/**
	 * Creates a new Source if it had not existed.
	 * 
	 * @param source
	 *            the source to be created
	 * @param meta
	 *            Slim3 meta-class for this type of {@link Source}s
	 * @return new {@link Source} or existing one
	 */
	public <E extends Source> E creatIfNotExist(E source, ModelMeta<E> meta) {
		Transaction tx = Datastore.beginTransaction();
		E theSource;
		try {
			Key key = KeyGen.genKey(source);
			theSource = Datastore.getOrNull(tx, meta, key);
			if (theSource == null) {
				source.setKey(key);
				source.setNextCheck(new Date());
				Datastore.put(tx, source);
				theSource = source;
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return theSource;
	}

	/**
	 * Creates a new {@link Region}.
	 * 
	 * @param region
	 *            {@link Region} to be created
	 */
	public void addRegion(Region region) {
		Key key = Datastore.put(region);
		region.setKey(key);
	}

	/**
	 * Gets {@link Source} by its key.
	 * 
	 * @param key
	 *            key of {@link Source}
	 * @return {@link Source}
	 */
	public Source getSourceByKey(Key key) {
		SourceMeta meta = SourceMeta.get();
		Source source = Datastore.getOrNull(meta, key);
		return source;
	}

	/**
	 * Gets list of {@link Source}s by their keys.
	 * 
	 * @param keys
	 *            iterable of keys of {@link Source}
	 * @return list of {@link Source}s
	 */
	public List<Source> getSourcesByKeys(Iterable<Key> keys) {
		SourceMeta meta = SourceMeta.get();
		List<Source> sources = Datastore.get(meta, keys);
		return sources;
	}

	/**
	 * Ensures a {@link ManualSource} exists for a user.
	 * 
	 * @param userId
	 *            user the check is performed for
	 * @return existing or newly created {@link ManualSource}
	 */
	public ManualSource ensureManualSource(String userId) {
		ManualSource manualSource = new ManualSource(userId);
		manualSource.setKey(KeyGen.genKey(manualSource));
		manualSource.setEnabled(false);
		manualSource.setUsage(0);
		return creatIfNotExist(manualSource, ManualSourceMeta.get());
	}

	/**
	 * Plans immediate {@link Source} check. It will be performed within a
	 * minute (depends on cron).
	 * 
	 * @param source
	 *            the {@link Source} the check is planned for
	 * @return date of new check
	 */
	public Date planSourceCheck(Source source) {
		if (source instanceof ManualSource) {
			// just ignore this request
			return source.getNextCheck();
		}
		Date date = new Date();
		source.setNextCheck(date);
		Datastore.put(source);
		return date;
	}

	/**
	 * Gets list of {@link Source}s to be checked in normal mode.
	 * 
	 * @return list of {@link Source}s
	 */
	public List<Source> getSourcesForNormalCheck() {
		int maxErrors =
			ConfigService.CONFIG_SERVICE.getConfig(
				ServerConfigKey.MAX_ERROR_SEQUENCE).get();
		SourceMeta meta = SourceMeta.get();
		List<Source> list =
			Datastore
				.query(meta)
				.filter(meta.enabled.equal(Boolean.TRUE))
				.filter(meta.nextCheck.lessThan(new Date()))
				.filterInMemory(meta.errorSequence.lessThan(maxErrors))
				.asList();
		return list;
	}

	/**
	 * Gets list of {@link Source}s to be checked in error mode.
	 * 
	 * @return list of {@link Source}s
	 */
	public List<Source> getSourcesForErrorCheck() {
		int maxErrors =
			ConfigService.CONFIG_SERVICE.getConfig(
				ServerConfigKey.MAX_ERROR_SEQUENCE).get();
		SourceMeta meta = SourceMeta.get();
		List<Source> list =
			Datastore
				.query(meta)
				.filter(meta.enabled.equal(Boolean.TRUE))
				.filter(meta.errorSequence.greaterThanOrEqual(maxErrors))
				.asList();
		return list;
	}

	/**
	 * Sets enqued flag and saves {@link Source} to database.
	 * 
	 * @param source
	 *            the {@link Source} the flag is to be changed for
	 * @param enqued
	 *            new value of enqued flag
	 */
	public void setEnqued(Source source, boolean enqued) {
		source.setEnqued(enqued);
		Datastore.put(source);
	}

	/**
	 * Saves a new {@link CheckStat} to database.
	 * 
	 * @param stat
	 *            {@link CheckStat} to be saved
	 */
	public void addStat(CheckStat stat) {
		Datastore.put(stat);
	}
}
