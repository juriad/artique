package cz.artique.client.artique;

import com.google.appengine.api.datastore.Key;
import com.google.code.gwteyecandy.Tooltip;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.listing.AbstractRowWidget;
import cz.artique.client.listing.RowWidget;
import cz.artique.client.listing.RowWidgetFactory;
import cz.artique.shared.model.item.UserItem;

public class UserItemRow extends AbstractRowWidget<UserItem, Key> {

	public static final UserItemRowFactory factory = new UserItemRowFactory();

	public static class UserItemRowFactory
			implements RowWidgetFactory<UserItem, Key> {

		public RowWidget<UserItem, Key> createWidget(UserItem data) {
			return new UserItemRow(data);
		}

	}

	private Label title;
	private Label content;

	public UserItemRow(UserItem data) {
		super(data);

		title = new Label(getData(false).getItemObject().getTitle());
		setHeader(title);

		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleExpanded();
			}
		});

		content = new Label(getData(false).getItemObject().getContent().getValue());
		setContent(content);

		Tooltip tt = new Tooltip();
		tt.setText(getData(false).getItemObject().getContentType().toString());
		tt.attachTo(content);
	}

	private void toggleExpanded() {
		if (isExpanded()) {
			collapse();
		} else {
			expand();
		}
	}

	@Override
	public void setNewData(UserItem e) {
		e.setItemObject(getData(true).getItemObject());
		super.setNewData(e);
	}

	@Override
	protected void newDataSet() {
		// TODO nothing yet
	}
}
