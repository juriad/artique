package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.RegionType;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientSourceService extends RemoteService {

	XMLSource addSource(XMLSource source);

	PageChangeSource addSource(PageChangeSource source);

	WebSiteSource addSource(WebSiteSource source);
	
	HTMLSource addSource(HTMLSource source);

	List<Region> getRegions(HTMLSource source, RegionType type);

	UserSource addUserSource(UserSource userSource);

	void updateUserSource(UserSource userSource);

	List<UserSource> getUserSources();

}
