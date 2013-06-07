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
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.Stats;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.utils.TransactionException;

public class SourceService {
	public List<Region> getAllRegions(Key source) {
		RegionMeta meta = RegionMeta.get();
		List<Region> list =
			Datastore
				.query(meta)
				.filter(meta.htmlSource.equal(source))
				.asList();
		return list;
	}

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
		} catch (Exception e) {
			throw new TransactionException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return theSource;
	}

	public void addRegion(Region region) {
		Key key = Datastore.put(region);
		region.setKey(key);
	}

	public Source getSourceByKey(Key key) {
		SourceMeta meta = SourceMeta.get();
		Source source = Datastore.getOrNull(meta, key);
		return source;
	}

	public List<Source> getSourcesByKeys(Iterable<Key> keys) {
		SourceMeta meta = SourceMeta.get();
		List<Source> sources = Datastore.get(meta, keys);
		return sources;
	}

	public ManualSource ensureManualSource() {
		String userId = UserService.getCurrentUserId();
		ManualSource manualSource = new ManualSource(userId);
		manualSource.setKey(KeyGen.genKey(manualSource));
		manualSource.setEnabled(false);
		manualSource.setUsage(0);
		return creatIfNotExist(manualSource, ManualSourceMeta.get());
	}

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

	public List<Source> getSourcesForNormalCheck() {
		int maxErrors =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.MAX_ERROR_SEQUENCE)
				.get();
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

	public List<Source> getSourcesForErrorCheck() {
		int maxErrors =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.MAX_ERROR_SEQUENCE)
				.get();
		SourceMeta meta = SourceMeta.get();
		List<Source> list =
			Datastore
				.query(meta)
				.filter(meta.enabled.equal(Boolean.TRUE))
				.filter(meta.errorSequence.greaterThanOrEqual(maxErrors))
				.asList();
		return list;
	}

	public void saveSource(Source source) {
		Datastore.put(source);
	}

	public void addStat(Stats stat) {
		Datastore.put(stat);
	}

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
