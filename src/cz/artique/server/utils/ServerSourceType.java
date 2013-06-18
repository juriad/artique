package cz.artique.server.utils;

import org.slim3.datastore.ModelMeta;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.server.service.ClientSourceServiceImpl;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;

/**
 * Wraps {@link SourceType} and adds attribute of type {@link ModelMeta} which
 * is used by {@link ClientSourceServiceImpl} to provide meta to underlying
 * service.
 * 
 * @author Adam Juraszek
 * 
 */
public enum ServerSourceType {
	/**
	 * Extends {@link SourceType#RSS_ATOM}
	 */
	RSS_ATOM(SourceType.RSS_ATOM, XMLSourceMeta.get()),
	/**
	 * Extends {@link SourceType#PAGE_CHANGE}
	 */
	PAGE_CHANGE(SourceType.PAGE_CHANGE, PageChangeSourceMeta.get()),
	/**
	 * Extends {@link SourceType#WEB_SITE}
	 */
	WEB_SITE(SourceType.WEB_SITE, WebSiteSourceMeta.get()),
	/**
	 * Extends {@link SourceType#MANUAL}
	 */
	MANUAL(SourceType.MANUAL, ManualSourceMeta.get());

	private final SourceType sourceType;
	private final ModelMeta<? extends Source> meta;

	private ServerSourceType(SourceType sourceType,
			ModelMeta<? extends Source> meta) {
		this.sourceType = sourceType;
		this.meta = meta;
	}

	/**
	 * @return source type
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @return meta for source of this type
	 */
	public ModelMeta<? extends Source> getMeta() {
		return meta;
	}

	/**
	 * Finds enum value by source type.
	 * 
	 * @param type
	 * @return
	 */
	public static ServerSourceType get(SourceType type) {
		for (ServerSourceType st : values()) {
			if (st.sourceType.equals(type)) {
				return st;
			}
		}
		return null;
	}
}
