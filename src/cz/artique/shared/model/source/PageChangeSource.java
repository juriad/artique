package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class PageChangeSource extends HTMLSource
		implements Serializable, GenKey, HasRegion {

	private static final long serialVersionUID = 1L;
	private Key region;

	private Text content;

	private Date lastChange;

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
		String region =
			getRegion() != null ? KeyFactory.keyToString(getRegion()) : "null";
		return SharedUtils.combineStringParts(prefix, url, region);
	}

	public Text getContent() {
		return content;
	}

	public void setContent(Text content) {
		this.content = content;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public RegionType getRegionType() {
		return RegionType.PAGE_CHANGE;
	}

}
