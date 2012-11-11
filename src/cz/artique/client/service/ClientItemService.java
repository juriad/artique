package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.item.Item;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientItemService extends RemoteService {
	List<Item> getItems();
}
