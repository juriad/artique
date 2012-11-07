package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;

@RemoteServiceRelativePath("service.s3gwt")
public interface Test1Service extends RemoteService {
	Source addSource(String url);
	
	List<Item> getItems();
}
