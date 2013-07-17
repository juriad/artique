package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.shared.model.label.Label;

public interface ClientLabelServiceAsync {

	void addLabel(Label label, AsyncCallback<Label> callback);

	void getAllLabels(AsyncCallback<List<Label>> callback);

	void updateLabels(List<Label> labels, AsyncCallback<Void> callback);

}
