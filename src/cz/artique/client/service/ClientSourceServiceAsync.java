package cz.artique.client.service;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

public interface ClientSourceServiceAsync {

	<E extends Source> void addSource(E source, AsyncCallback<E> callback);

	void addUserSource(UserSource userSource, AsyncCallback<UserSource> callback);

	void checkRegion(Region region, AsyncCallback<Region> callback);

	void getRecommendation(AsyncCallback<Recommendation> callback);

	void getRegions(Key source, AsyncCallback<List<Region>> callback);

	void getUserSources(AsyncCallback<List<UserSource>> callback);

	void planSourceCheck(Key source, AsyncCallback<Date> callback);

	void updateUserSource(UserSource userSource,
			AsyncCallback<UserSource> callback);

}
