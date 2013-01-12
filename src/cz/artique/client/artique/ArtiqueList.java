package cz.artique.client.artique;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import cz.artique.client.listing.RowWidgetFactory;
import cz.artique.client.listing.ScrollEndEvent;
import cz.artique.client.listing.ScrollEndEvent.ScrollEndType;
import cz.artique.client.listing.WidgetList;
import cz.artique.shared.model.item.UserItem;

public class ArtiqueList extends WidgetList<UserItem, Key> {

	public ArtiqueList(RowWidgetFactory<UserItem, Key> factory) {
		super(factory);

		scrollPanel.setWidth("100%");
		scrollPanel.setHeight(Window.getClientHeight() + "px");
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
				scrollPanel.setHeight(height + "px");
			}
		});

		scrollPanel.addScrollHandler(new ScrollHandler() {
			public void onScroll(ScrollEvent event) {
				int pos = scrollPanel.getVerticalScrollPosition();
				if (pos == scrollPanel.getMinimumVerticalScrollPosition()) {
					fireEvent(new ScrollEndEvent(ScrollEndType.TOP));
				} else if (pos == scrollPanel
					.getMaximumVerticalScrollPosition()) {
					fireEvent(new ScrollEndEvent(ScrollEndType.BOTTOM));
				}
			}
		});
	}
	
	@Override
	protected void fetchToFillPage() {
		super.fetchToFillPage();
		getProvider().fetch(-1);
	}
}
