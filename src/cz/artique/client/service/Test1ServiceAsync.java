package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;

public interface Test1ServiceAsync {

	void addSource(String url, AsyncCallback<Source> callback);

	void getItems(AsyncCallback<List<Item>> callback);

}
