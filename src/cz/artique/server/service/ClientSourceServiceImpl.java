package cz.artique.server.service;

import java.util.List;

import cz.artique.client.service.ClientSourceService;
import cz.artique.server.meta.source.HTMLSourceMeta;
import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.hierarchy.Hierarchy;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

// TODO sanitize whole class: only serializable exceptions
public class ClientSourceServiceImpl implements ClientSourceService {

	public XMLSource addSource(XMLSource source) {
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, XMLSourceMeta.get());
	}

	public ManualSource addSource(ManualSource source) {
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, ManualSourceMeta.get());
	}

	public HTMLSource addSource(HTMLSource source) {
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, HTMLSourceMeta.get());
	}

	public PageChangeSource addSource(PageChangeSource source) {
		SourceService ss = new SourceService();
		if (source.getRegion() == null) {
			if (source.getRegionObject() == null) {
				// TODO throw exception
				return null;
			} else {
				Region theRegion =
					ss.addRegionIfNotExist(source.getRegionObject());
				source.setRegion(theRegion.getKey());
			}
		}
		return ss.creatIfNotExist(source, PageChangeSourceMeta.get());
	}

	public WebSiteSource addSource(WebSiteSource source) {
		SourceService ss = new SourceService();
		if (source.getRegion() == null) {
			if (source.getRegionObject() == null) {
				// TODO throw exception
				return null;
			} else {
				Region theRegion =
					ss.addRegionIfNotExist(source.getRegionObject());
				source.setRegion(theRegion.getKey());
			}
		}
		return ss.creatIfNotExist(source, WebSiteSourceMeta.get());
	}

	public List<Region> getRegions(HTMLSource source) {
		SourceService ss = new SourceService();
		return ss.getRegions(source);
	}

	public UserSource watchSource(Source source) {
		// TODO Auto-generated method stub
		return null;
	}

	public void unwatchSource(UserSource source) {
		// TODO Auto-generated method stub

	}

	public Hierarchy<UserSource> getHierarchy() {
		// TODO Auto-generated method stub
		return null;
	}

	public Hierarchy<UserSource> updateHierarchy(Hierarchy<UserSource> hierarchy) {
		// TODO Auto-generated method stub
		return null;
	}

}
