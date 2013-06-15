package cz.artique.shared.model.item;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.server.crawler.ManualCrawler;
import cz.artique.shared.model.source.Source;

/**
 * Type of {@link Item} used to store items manually added by user. Although
 * {@link ManualCrawler} exists, it is not useful. All {@link ManualItem}s are
 * added/created by user. {@link ManualItem} extends general {@link Item}; does
 * not add any attributes.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class ManualItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for slim3 framework.
	 */
	public ManualItem() {}

	/**
	 * Constructor just calling constructor of super-type:
	 * {@link Item#Item(Source)}.
	 * 
	 * @param source
	 */
	public ManualItem(Source source) {
		super(source);
	}

}
