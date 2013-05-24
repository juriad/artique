package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public interface ClientSourceServiceAsync {

	void addSource(XMLSource source, AsyncCallback<XMLSource> callback);

	void addSource(PageChangeSource source,
			AsyncCallback<PageChangeSource> callback);

	void addSource(WebSiteSource source, AsyncCallback<WebSiteSource> callback);

	void getRegions(HTMLSource source, AsyncCallback<List<Region>> callback);

	void addUserSource(UserSource userSource, AsyncCallback<UserSource> callback);

	void updateUserSource(UserSource userSource, AsyncCallback<Void> callback);

	void getUserSources(AsyncCallback<List<UserSource>> callback);
}
