package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class PageChangeSource extends HTMLSource
		implements Serializable, GenKey {

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

	public Region getRegionObject() {
		return regionObject;
	}

	public void setRegionObject(Region regionObject) {
		this.regionObject = regionObject;
	}

	@Override
	public Key getKeyParent() {
		return getParent();
	}

	@Override
	public String getKeyName() {
		String prefix = "PAGE_CHANGE";
		String url = getUrl().getValue();
		String region = KeyFactory.keyToString(getRegion());
		return SharedUtils.combineStringParts(prefix, url, region);
	}

}
