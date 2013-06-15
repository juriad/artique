package cz.artique.shared.model.source;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.sources.SourceTypePicker;
import cz.artique.client.sources.SourcesConstants;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.LinkItem;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.PageChangeItem;

/**
 * Categorize source to types. This enum is often used as a lookup table for
 * source class. Because of nature of datastore, there is no easy way how to
 * determine class of referenced object from datastore {@link Key}, therefore
 * attribute of type SourceType exists in {@link UserSource} to provide this
 * information.
 * 
 * <p>
 * The only other information this enum provides is whether this source type
 * supports regions: only HTML based sources do.
 * 
 * This enum also lists all possible states of {@link SourceTypePicker}. And
 * also provides (method {@link #name()}) part of name for internationalization
 * in {@link SourcesConstants}.
 * 
 * @author Adam Juraszek
 * 
 */
public enum SourceType {
	/**
	 * RSS/Atom based source, produces {@link ArticleItem}s
	 */
	RSS_ATOM(XMLSource.class, false),

	/**
	 * HTML based source, produces {@link PageChangeItem}s
	 */
	PAGE_CHANGE(PageChangeSource.class, true),

	/**
	 * HTML based source, produces {@link LinkItem}s
	 */
	WEB_SITE(WebSiteSource.class, true),

	/**
	 * Manual source, only user add new items of type {@link ManualItem}
	 */
	MANUAL(ManualSource.class, false);

	private final Class<?> clazz;
	private final boolean supportRegion;

	private SourceType(Class<?> clazz, boolean supportRegion) {
		this.clazz = clazz;
		this.supportRegion = supportRegion;
	}

	/**
	 * 
	 * @param clazz
	 * @return SourceType if clazz is class of some descendant of {@link Source}
	 */
	public static SourceType get(Class<?> clazz) {
		for (SourceType st : values()) {
			if (st.clazz.equals(clazz)) {
				return st;
			}
		}
		return null;
	}

	/**
	 * @return class of source with this type
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @return region type of this source, null if source does not support
	 *         regions
	 */
	public boolean isSupportRegion() {
		return supportRegion;
	}

}
