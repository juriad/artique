package cz.artique.client.leftPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.labels.LabelsDialog;
import cz.artique.client.shortcuts.ShortcutsDialog;

/**
 * Options panel inside {@link LeftPanel}; it contains configuration, user login
 * and documentation.
 * 
 * @author Adam Juraszek
 * 
 */
public class OptionPanel extends Composite {

	private static final String docUrl = "http://userdoc.artique.cz/intro";

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("OptionPanel.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	private static OptionUiBinder uiBinder = GWT.create(OptionUiBinder.class);

	interface OptionUiBinder extends UiBinder<Widget, OptionPanel> {}

	@UiField
	InlineLabel userName;

	@UiField
	Button logout;

	@UiField
	Button editLabelsButton;

	@UiField
	Button editShortcutsButton;

	@UiField
	Button doc;

	public OptionPanel() {
		res.style().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		userName.setText(ArtiqueWorld.WORLD.getUserInfo().getNickname());
	}

	/**
	 * When edit labels button was clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("editLabelsButton")
	protected void editLabelsButtonClicked(ClickEvent event) {
		LabelsDialog.DIALOG.showDialog();
	}

	/**
	 * When edit shortcuts button was clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("editShortcutsButton")
	protected void editShortcutsButtonClicked(ClickEvent event) {
		ShortcutsDialog.DIALOG.showDialog();
	}

	/**
	 * When logout button was clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("logout")
	protected void logoutClicked(ClickEvent event) {
		String url = ArtiqueWorld.WORLD.getUserInfo().getLogoutUrl();
		Window.Location.assign(url);
	}

	/**
	 * When browse documentation button was clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("doc")
	protected void docClicked(ClickEvent event) {
		Window.open(docUrl, "_blank", "");
	}
}
