package cz.artique.server.service;

import org.slim3.datastore.ModelMeta;

import cz.artique.server.meta.source.HTMLSourceMeta;
import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.source.Source;

public enum SourceType {
	HTML_SOURCE(HTMLSourceMeta.get()),
	PAGE_CHANGE(PageChangeSourceMeta.get()),
	WEB_SITE(WebSiteSourceMeta.get()),
	MANUAL(ManualSourceMeta.get()),
	XML_SOURCE(XMLSourceMeta.get());

	private final ModelMeta<? extends Source> meta;

	private SourceType(ModelMeta<? extends Source> meta) {
		this.meta = meta;
	}

	public ModelMeta<? extends Source> getMeta() {
		return meta;
	}
}
