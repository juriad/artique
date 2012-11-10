package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.hierarchy.Hierarchy;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

public interface ClientSourceServiceAsync {

	void addSource(XMLSource source, AsyncCallback<XMLSource> callback);

	void addSource(ManualSource source, AsyncCallback<ManualSource> callback);

	void addSource(PageChangeSource source,
			AsyncCallback<PageChangeSource> callback);

	void addSource(WebSiteSource source, AsyncCallback<WebSiteSource> callback);

	void getRegions(HTMLSource source, AsyncCallback<List<Region>> callback);

	void watchSource(Source source, AsyncCallback<UserSource> callback);

	void unwatchSource(UserSource source, AsyncCallback<Void> callback);

	void getHierarchy(AsyncCallback<Hierarchy<UserSource>> callback);

	void updateHierarchy(Hierarchy<UserSource> hierarchy,
			AsyncCallback<Hierarchy<UserSource>> callback);

}
