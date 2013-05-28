package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class WebSiteSource extends HTMLSource implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	public WebSiteSource() {}

	public WebSiteSource(Link url) {
		super(url);
	}

	public String getKeyName() {
		String prefix = "WEB_SITE";
		String url = getUrl().getValue();
		return SharedUtils.combineStringParts(prefix, url);
	}
}
