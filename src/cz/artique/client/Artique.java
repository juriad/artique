package cz.artique.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.artiqueHierarchy.ArtiqueSourcesTree;
import cz.artique.client.artiqueListing.ArtiqueList;
import cz.artique.client.artiqueListing.UserItemRow;

public class Artique extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Artique> {}

	@UiField(provided = true)
	ArtiqueList list;

	@UiField
	Label userName;

	@UiField
	Anchor logout;

	@UiField
	ArtiqueSourcesTree sources;

	private static Resources resources;

	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}

	public Artique() {
		list = new ArtiqueList(UserItemRow.factory);
		initWidget(uiBinder.createAndBindUi(this));
		ArtiqueWorld.WORLD.setList(list);
		
		userName.setText(ArtiqueWorld.WORLD.getUser().getNickname());
		logout.setHref(ArtiqueWorld.WORLD.getUserInfo().getLogoutUrl());
	}
}
