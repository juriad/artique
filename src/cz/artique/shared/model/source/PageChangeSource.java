package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;

import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.service.SourceType;
import cz.artique.shared.utils.Utils;

@Model(schemaVersion = 1)
public class PageChangeSource extends HTMLSource implements Serializable {

	private static final long serialVersionUID = 1L;
	private Key region;

	@Attribute(persistent = false)
	private Region regionObject;

	public PageChangeSource() {}

	public PageChangeSource(Link url, Key parent, Key region) {
		super(url, parent);
		this.setRegion(region);
	}

	public Key getRegion() {
		return region;
	}

	public void setRegion(Key region) {
		this.region = region;
	}

	@Override
	public Key genKey() {
		String prefix = SourceType.PAGE_CHANGE.name();
		String url = getUrl().getValue();
		String region = KeyFactory.keyToString(getRegion());
		return Datastore.createKey(getParent(), PageChangeSourceMeta.get(),
			Utils.combineStringParts(prefix, url, region));
	}

	public Region getRegionObject() {
		return regionObject;
	}

	public void setRegionObject(Region regionObject) {
		this.regionObject = regionObject;
	}

}
