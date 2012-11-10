package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.service.SourceType;
import cz.artique.shared.utils.Utils;

@Model(schemaVersion = 1)
public class HTMLSource extends Source implements Serializable {

	private static final long serialVersionUID = 1L;

	public HTMLSource() {}

	public HTMLSource(Link url) {
		this(url, null);
	}

	public HTMLSource(Link url, Key parent) {
		super(url, parent);
	}

	@Override
	public Key genKey() {
		String prefix = SourceType.HTML_SOURCE.name();
		String url = getUrl().getValue();
		return Datastore.createKey(ManualSourceMeta.get(),
			Utils.combineStringParts(prefix, url));
	}

}
