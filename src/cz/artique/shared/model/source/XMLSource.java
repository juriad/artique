package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Link;

@Model(schemaVersion = 1)
public class XMLSource extends Source implements Serializable {

	private static final long serialVersionUID = 1L;

	public XMLSource() {}

	public XMLSource(Link url) {
		super(url);
	}

}
