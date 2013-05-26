package cz.artique.server.utils;

import org.slim3.datastore.ModelMeta;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;

public enum ServerSourceType {
	RSS_ATOM(SourceType.RSS_ATOM, XMLSourceMeta.get()),
	PAGE_CHANGE(SourceType.PAGE_CHANGE, PageChangeSourceMeta.get()),
	WEB_SITE(SourceType.WEB_SITE, WebSiteSourceMeta.get()),
	MANUAL(SourceType.MANUAL, ManualSourceMeta.get());

	private final SourceType sourceType;
	private final ModelMeta<? extends Source> meta;

	private ServerSourceType(SourceType sourceType,
			ModelMeta<? extends Source> meta) {
		this.sourceType = sourceType;
		this.meta = meta;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public ModelMeta<? extends Source> getMeta() {
		return meta;
	}

	public static ServerSourceType get(SourceType type) {
		for (ServerSourceType st : values()) {
			if (st.sourceType.equals(type)) {
				return st;
			}
		}
		return null;
	}
}
