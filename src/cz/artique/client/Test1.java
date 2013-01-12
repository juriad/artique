package cz.artique.client;

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

import cz.artique.client.artique.ArtiqueList;
import cz.artique.client.artique.ArtiqueListProvider;
import cz.artique.client.artique.UserItemRow;
import cz.artique.client.listing.ListingSettings;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.XMLSource;

public class Test1 extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Test1> {}

	ClientSourceServiceAsync css = GWT.create(ClientSourceService.class);

	Timer timer;

	@UiField(provided = true)
	ArtiqueList items;

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

	int itemsCount = -1;

	private UserInfo userInfo;

	private static Resources resources;

	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}

	public Test1(UserInfo userInfo) {
		items = new ArtiqueList(UserItemRow.factory);

		ListingSettings settings =
			new ListingSettings(null, 20, null, 5, 20000, 5000);
		final ArtiqueListProvider provider = new ArtiqueListProvider(settings, items);

		initWidget(uiBinder.createAndBindUi(this));
		this.userInfo = userInfo;

		userName.setText(userInfo.getUser().getNickname());
		logout.setHref(userInfo.getLogoutUrl());

		css.getUserSources(new GetSourcesCallback());

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

			public void onFailure(Throwable caught) {}
		});
	}

	class AddXMLSourceCallback implements AsyncCallback<UserSource> {

		public void onFailure(Throwable caught) {
			sources.clear();
			sources.setHTML(0, 0, "Error");
		}

		public void onSuccess(UserSource result) {
			css.getUserSources(new GetSourcesCallback());
		}
	}

	class GetSourcesCallback implements AsyncCallback<List<UserSource>> {

		public void onFailure(Throwable caught) {
			sources.clear();
			sources.setHTML(0, 0, "Error");
		}

		public void onSuccess(List<UserSource> result) {
			sources.clear();
			for (int i = 0; i < result.size(); i++) {
				UserSource us = result.get(i);
				sources.setHTML(i, 0, us.getName());
				sources.setHTML(i, 1, us.getHierarchy());
			}
		}
	}
}
