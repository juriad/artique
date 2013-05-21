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
public class WebSiteSource extends HTMLSource
		implements Serializable, GenKey, HasRegion {

	private static final long serialVersionUID = 1L;
	private Key region;

	@Attribute(persistent = false)
	private Region regionObject;

	public WebSiteSource() {}

	public WebSiteSource(Link url, Key parent, Key region) {
		super(url, parent);
		setRegion(region);
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
		String prefix = "WEB_SITE";
		String url = getUrl().getValue();
		String region =
			getRegion() != null ? KeyFactory.keyToString(getRegion()) : "null";
		return SharedUtils.combineStringParts(prefix, url, region);
	}

	public RegionType getRegionType() {
		return RegionType.WEB_SITE;
	}
}
