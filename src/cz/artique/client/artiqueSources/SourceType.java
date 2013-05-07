package cz.artique.client.artiqueSources;

import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.RegionType;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public enum SourceType {
	RSS_ATOM(XMLSource.class, true, null),
	PAGE_CHANGE(PageChangeSource.class, true, RegionType.PAGE_CHANGE),
	WEB_SITE(WebSiteSource.class, true, RegionType.WEB_SITE),
	MANUAL(ManualSource.class, false, null);

	private final Class<?> clazz;
	private final boolean editable;
	private final RegionType regionType;

	private SourceType(Class<?> clazz, boolean editable, RegionType regionType) {
		this.clazz = clazz;
		this.editable = editable;
		this.regionType = regionType;
	}

	public static SourceType get(Class<?> clazz) {
		for (SourceType st : values()) {
			if (st.clazz.equals(clazz)) {
				return st;
			}
		}
		return null;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public boolean isEditable() {
		return editable;
	}

	public RegionType getRegionType() {
		return regionType;
	}

}
