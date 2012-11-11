package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class HTMLSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	public HTMLSource() {}

	public HTMLSource(Link url) {
		this(url, null);
	}

	public HTMLSource(Link url, Key parent) {
		super(url, parent);
	}

	public Key getKeyParent() {
		return null;
	}

	public String getKeyName() {
		String prefix = "HTML_SOURCE";
		String url = getUrl().getValue();
		return SharedUtils.combineStringParts(prefix, url);
	}

}
