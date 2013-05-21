package cz.artique.shared.model.source;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.HasKey;

public interface HasRegion extends HasKey<Key> {
	Region getRegionObject();

	void setRegionObject(Region region);

	Key getRegion();

	void setRegion(Key regionObject);

	RegionType getRegionType();
}
