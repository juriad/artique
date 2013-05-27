package cz.artique.server.service;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.RegionMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.utils.TransactionException;

public class SourceService {
	public List<Region> getRegions(Key source) {
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
			Key key = ServerUtils.genKey(source);
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

	public Region getRegionByKey(Key key) {
		RegionMeta meta = RegionMeta.get();
		Region regionObject = Datastore.getOrNull(meta, key);
		return regionObject;
	}

	public ManualSource ensureManualSource() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ManualSource manualSource = new ManualSource(user);
		manualSource.setKey(ServerUtils.genKey(manualSource));
		manualSource.setEnabled(false);
		manualSource.setUsage(0);
		return creatIfNotExist(manualSource, ManualSourceMeta.get());
	}

	public Date planSourceCheck(Key source) {
		Source sourceByKey = getSourceByKey(source);
		if (sourceByKey instanceof ManualSource) {
			throw new IllegalArgumentException(
				"Source must not be a manual source");
		}
		Date date = new Date();
		sourceByKey.setNextCheck(date);
		Datastore.put(sourceByKey);
		return date;
	}
}
