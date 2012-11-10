package cz.artique.server.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.server.meta.source.RegionMeta;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;

public class SourceService {
	public List<Region> getRegions(HTMLSource source) {
		RegionMeta meta = RegionMeta.get();
		List<Region> list =
			Datastore
				.query(meta)
				.filter(meta.htmlSource.equal(source.getKey()))
				.asList();
		return list;
	}

	public Link getLink(String url) {
		try {
			new URL(url); // test url
			return new Link(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public <E extends Source> E creatIfNotExist(E source, ModelMeta<E> meta) {
		Transaction tx = Datastore.beginTransaction();
		Key key = source.genKey();
		E theSource = (E) Datastore.getOrNull(tx, meta, key);
		if (theSource == null) {
			source.setKey(key);
			source.setNextCheck(new Date());
			Datastore.put(tx, source);
			theSource = source;
		}
		tx.commit();
		return theSource;
	}

	public Region addRegionIfNotExist(Region region) {
		Transaction tx = Datastore.beginTransaction();
		Key key = region.genKey();
		Region theRegion =
			(Region) Datastore.getOrNull(tx, RegionMeta.get(), key);
		if (theRegion == null) {
			region.setKey(key);
			Datastore.put(tx, region);
			theRegion = region;
		}
		tx.commit();
		return theRegion;
	}

}
