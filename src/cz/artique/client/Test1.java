package cz.artique.client;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.service.ClientItemService;
import cz.artique.client.service.ClientItemServiceAsync;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.XMLSource;

public class Test1 extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Test1> {}

	ClientItemServiceAsync cis = GWT.create(ClientItemService.class);

	ClientSourceServiceAsync css = GWT.create(ClientSourceService.class);

	Timer timer;

	@UiField
	FlexTable items;

	@UiField
	FlexTable sources;

	@UiField
	TextBox name;

	@UiField
	TextBox url;

	@UiField
	Label userName;

	@UiField
	Anchor logout;

	@UiField
	FlexTable logs;

	int itemsCount = -1;

	private UserInfo userInfo;

	public Test1(UserInfo userInfo) {
		initWidget(uiBinder.createAndBindUi(this));
		this.userInfo = userInfo;

		userName.setText(userInfo.getUser().getNickname());
		logout.setHref(userInfo.getLogoutUrl());

		css.getUserSources(new GetSourcesCallback());

		timer = new Timer() {
			@Override
			public void run() {
				cis.getItems(new GetItemsCallback());
			}
		};
		timer.scheduleRepeating(5000);
	}

	private void log(String status, String component, String message) {
		int index = logs.insertRow(0);
		logs.setHTML(index, 0, new Date().toString());
		logs.setHTML(index, 1, status);
		logs.setHTML(index, 2, component);
		logs.setHTML(index, 3, message);
	}

	class GetItemsCallback implements AsyncCallback<List<UserItem>> {

		public void onSuccess(List<UserItem> result) {
			if (result.size() == itemsCount) {
				log("NEUTRAL", "items", "no new items");
				return;
			}
			itemsCount = result.size();

			items.clear();
			for (int i = 0; i < result.size(); i++) {
				Item it = result.get(i).getItemObject();
				items.setHTML(i, 0, it.getTitle());
				items.setHTML(i, 1, it.getContent().getValue());
				items.setHTML(i, 2, it.getAdded().toString());
				if (it instanceof ArticleItem) {
					ArticleItem a = (ArticleItem) it;
					Date pub = a.getPublished();
					items.setHTML(i, 3, pub == null ? "-" : pub.toString());
				} else {
					items.setHTML(i, 3, "--");
				}
			}
			log("SUCCESS", "items", "refreshed; got " + result.size()
				+ " items");
		}

		public void onFailure(Throwable caught) {
			items.clear();
			items.setHTML(0, 0, "Error");
			log("ERROR", "items", caught.getLocalizedMessage());
		}
	}

	@UiHandler("add")
	void handleClick(ClickEvent e) {
		XMLSource source = new XMLSource(new Link(url.getText()));

		css.addSource(source, new AsyncCallback<XMLSource>() {

			public void onSuccess(XMLSource result) {
				UserSource us = new UserSource();
				us.setName(name.getText());
				us.setSource(result.getKey());
				us.setUser(userInfo.getUser());
				us.setWatching(true);

				css.addUserSource(us, new AddXMLSourceCallback());
			}

			public void onFailure(Throwable caught) {
				log("ERROR", "add", caught.getLocalizedMessage());
			}
		});
	}

	class AddXMLSourceCallback implements AsyncCallback<UserSource> {

		public void onFailure(Throwable caught) {
			sources.clear();
			sources.setHTML(0, 0, "Error");
			log("ERROR", "add", caught.getLocalizedMessage());
		}

		public void onSuccess(UserSource result) {
			log("SUCCESS", "add", "source has been added");
			css.getUserSources(new GetSourcesCallback());
		}
	}

	class GetSourcesCallback implements AsyncCallback<List<UserSource>> {

		public void onFailure(Throwable caught) {
			sources.clear();
			sources.setHTML(0, 0, "Error");
			log("ERROR", "sources", caught.getLocalizedMessage());
		}

		public void onSuccess(List<UserSource> result) {
			sources.clear();
			for (int i = 0; i < result.size(); i++) {
				UserSource us = result.get(i);
				sources.setHTML(i, 0, us.getName());
				sources.setHTML(i, 1, us.getHierarchy());
			}
			log("SUCCESS", "sources", "got " + result.size() + " sources");
		}
	}
}
