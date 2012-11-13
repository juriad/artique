package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.Label;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientLabelService extends RemoteService {

	List<Label> getAllLabels();

	Label addLabel(Label label);

}
