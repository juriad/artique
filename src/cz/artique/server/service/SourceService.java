package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.RegionMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.WebSiteSource;

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

	public <E extends Source> E creatIfNotExist(E source, ModelMeta<E> meta) {
		Transaction tx = Datastore.beginTransaction();
		Key key = ServerUtils.genKey(source);
		E theSource = Datastore.getOrNull(tx, meta, key);
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
		Key key = ServerUtils.genKey(region);
		Region theRegion = Datastore.getOrNull(tx, RegionMeta.get(), key);
		if (theRegion == null) {
			region.setKey(key);
			Datastore.put(tx, region);
			theRegion = region;
		}
		tx.commit();
		return theRegion;
	}

	public List<PageChangeSource> getPageChangeSources(HTMLSource parent) {
		PageChangeSourceMeta meta = PageChangeSourceMeta.get();
		List<PageChangeSource> list =
			Datastore
				.query(meta)
				.filter(meta.parent.equal(parent.getKey()))
				.filter(meta.enabled.equal(Boolean.TRUE))
				.asList();
		List<Key> regionKeys = new ArrayList<Key>();
		for (PageChangeSource s : list) {
			regionKeys.add(s.getRegion());
		}
		RegionMeta rMeta = RegionMeta.get();
		List<Region> regions = Datastore.get(rMeta, regionKeys);

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRegionObject(regions.get(i));
		}

		return list;
	}

	public List<WebSiteSource> getWebSiteSource(HTMLSource parent) {
		WebSiteSourceMeta meta = WebSiteSourceMeta.get();
		List<WebSiteSource> list =
			Datastore
				.query(meta)
				.filter(meta.parent.equal(parent.getKey()))
				.filter(meta.enabled.equal(Boolean.TRUE))
				.asList();
		List<Key> regionKeys = new ArrayList<Key>();
		for (WebSiteSource s : list) {
			regionKeys.add(s.getRegion());
		}
		RegionMeta rMeta = RegionMeta.get();
		List<Region> regions = Datastore.get(rMeta, regionKeys);

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRegionObject(regions.get(i));
		}

		return list;
	}
}
