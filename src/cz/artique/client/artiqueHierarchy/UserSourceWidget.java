package cz.artique.client.artiqueHierarchy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;

import cz.artique.shared.model.source.UserSource;

public class UserSourceWidget extends Composite {

	private final UserSource userSource;

	public UserSourceWidget(UserSource userSource) {
		this.userSource = userSource;
		Anchor source = new Anchor(userSource.getName());
		initWidget(source);
		source.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// TODO add history item
			}
		});
	}

	public UserSource getUserSource() {
		return userSource;
	}
}
