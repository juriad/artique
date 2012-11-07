package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.model.source.UserSource;

@Model(schemaVersion = 1)
public class HTMLUserSource extends UserSource implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Watched region
	 */
	private Key region;

	/**
	 * Type of this source
	 */
	private HTMLSourceType type;

	public HTMLUserSource() {}

	public HTMLUserSource(User user, Source source, String name,
			HTMLSourceType type) {
		super(user, source, name);
		setType(type);
		setRegion(null);
	}

	public Key getRegion() {
		return region;
	}

	public HTMLSourceType getType() {
		return type;
	}

	public void setRegion(Key region) {
		this.region = region;
	}

	public void setType(HTMLSourceType type) {
		this.type = type;
	}

}
