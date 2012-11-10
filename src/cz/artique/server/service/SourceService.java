package cz.artique.server.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.server.meta.source.RegionMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.HTMLSourceType;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.XMLSource;
import cz.artique.utils.SourceException;

public class SourceService {
	public Source createSource(Link url, SourceType type) {
		Source s = null;
		switch (type) {
		case HTML_SOURCE:
			s = new HTMLSource(url);
			break;
		case MANUAL:
			throw new SourceException("cannot create manual source this way");
		case XML_SOURCE:
			s = new XMLSource(url);
			break;
		}

		s.setNextCheck(new Date());

		Key key = Datastore.put(s);
		s.setKey(key);
		return s;
	}

	public List<Region> getRegions(HTMLSource source, HTMLSourceType type) {
		RegionMeta meta = RegionMeta.get();
		List<Region> list =
			Datastore
				.query(meta)
				.filter(meta.htmlSource.equal(source.getKey()))
				.filter(meta.type.equal(type))
				.asList();
		return list;
	}

	public Source getSource(Link url) {
		SourceMeta meta = SourceMeta.get();
		List<Source> list =
			Datastore.query(meta).filter(meta.url.equal(url)).asList();
		if (list.size() == 0) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new SourceException("There are " + list.size()
				+ " sources with the same url");
		}
	}

	public Link getLink(String url) {
		try {
			new URL(url); // test url
			return new Link(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
