package cz.artique.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.service.Test1Service;
import cz.artique.client.service.Test1ServiceAsync;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;

public class Test1 extends Composite {

	@UiField
	TextBox source;

	@UiField
	FlexTable list;

	@UiField
	Label msg;

	@UiField
	Anchor signOut;

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);
	private static Test1ServiceAsync service = GWT.create(Test1Service.class);

	interface MainUiBinder extends UiBinder<Widget, Test1> {}

	public Test1(UserInfo userInfo) {
		initWidget(uiBinder.createAndBindUi(this));
		signOut.setHref(userInfo.getLogoutUrl());
	}

	@UiHandler("add")
	void add(ClickEvent e) {
		service.addSource(source.getText(), new AsyncCallback<Source>() {

			public void onSuccess(Source result) {
				msg.setText("added source " + result.getUrl().getValue());
			}

			public void onFailure(Throwable caught) {
				msg.setText("source is invalid");
			}
		});
	}

	@UiHandler("refresh")
	void refresh(ClickEvent e) {
		service.getItems(new AsyncCallback<List<Item>>() {

			public void onFailure(Throwable caught) {
				msg.setText("failed to refresh");
			}

			public void onSuccess(List<Item> result) {
				msg.setText("loaded items: " + result.size());
				list.clear();

				for (int i = 0; i < result.size(); i++) {
					Item item = result.get(i);
					list.setHTML(i, 0, item.getTitle());
					list.setHTML(i, 1, item.getContent().getValue());
				}
			}
		});
	}

}
