package cz.artique.client.service;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientSourceService extends RemoteService {

	<E extends Source> E addSource(E source);

	List<Region> getRegions(Key source);

	UserSource addUserSource(UserSource userSource);

	UserSource updateUserSource(UserSource userSource);

	List<UserSource> getUserSources();

	boolean checkRegion(Region region);

}
