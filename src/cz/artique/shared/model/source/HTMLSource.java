package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;

/**
 * Trivial extension of {@link Source}. Does not add anything, just exist to
 * support source polymorphism.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public abstract class HTMLSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for slim3 framework.
	 */
	public HTMLSource() {}

	/**
	 * Constructs HTMLSource from URL; called by subclasses.
	 * 
	 * @param url
	 *            URL of source
	 */
	protected HTMLSource(Link url) {
		super(url);
	}
}
