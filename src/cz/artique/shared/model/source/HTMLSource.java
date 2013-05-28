package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;

@Model(schemaVersion = 1)
public abstract class HTMLSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	public HTMLSource() {}

	public HTMLSource(Link url) {
		super(url);
	}
}
