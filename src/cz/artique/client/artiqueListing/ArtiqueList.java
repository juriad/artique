package cz.artique.client.artiqueListing;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import cz.artique.client.listing.RowWidget;
import cz.artique.client.listing.RowWidgetFactory;
import cz.artique.client.listing.ScrollEndEvent;
import cz.artique.client.listing.ScrollEndEvent.ScrollEndType;
import cz.artique.client.listing.ScrollEndHandler;
import cz.artique.client.listing.WidgetList;
import cz.artique.shared.model.item.UserItem;

public class ArtiqueList extends WidgetList<UserItem, Key> {

	public class SelectionHandler implements Handler {
		public void onSelectionChange(SelectionChangeEvent event) {
			RowWidget<UserItem, Key> selectedRowWidget = getSelectedRowWidget();
			if (selectedRowWidget != null) {
				int offsetTop =
					selectedRowWidget.asWidget().getElement().getOffsetTop();
				int maxScrollTop =
					scrollPanel.getMaximumVerticalScrollPosition();
				scrollPanel.setVerticalScrollPosition(Math.min(offsetTop,
					maxScrollTop));
			}
		}
	}

	private boolean resetScrollToTop;

	public class EndHandler implements ScrollEndHandler {

		public void onScrollEnd(ScrollEndEvent event) {
			if (resetScrollToTop) {
				resetScrollToTop = false;
				scrollPanel.scrollToTop();
				return;
			}
			if (ScrollEndType.NEAR_BOTTOM.equals(event.getScrollEndType())
				|| ScrollEndType.BOTTOM.equals(event.getScrollEndType())) {
				if (!isRowCountExact()) {
					GWT.log("near bottom or bottom");
					getProvider().fetch(-1);
				}
			}
		}
	}

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
				int min = scrollPanel.getMinimumVerticalScrollPosition();
				int max = scrollPanel.getMaximumVerticalScrollPosition();

				int height = scrollPanel.getElement().getClientHeight();
				float rate = 0.2f;

				if (min >= max) {
					return;
				}

				if (pos == min) {
					fireEvent(new ScrollEndEvent(ScrollEndType.TOP));
				} else if (pos < min + height * rate) {
					fireEvent(new ScrollEndEvent(ScrollEndType.NEAR_TOP));
				} else if (pos == max) {
					fireEvent(new ScrollEndEvent(ScrollEndType.BOTTOM));
				} else if (pos > max - height * rate) {
					fireEvent(new ScrollEndEvent(ScrollEndType.NEAR_BOTTOM));
				}
			}
		});
		
		this.addSelectionChangeHandler(new SelectionHandler());

		this.addScrollEndHandler(new EndHandler());
	}

	@Override
	public void clear() {
		super.clear();
		resetScrollToTop = true;
	}

	@Override
	protected void rowsAdded() {
		super.rowsAdded();
		if (scrollPanel.getMaximumVerticalScrollPosition() <= 0) {
			getProvider().fetch(-1);
		}
	}
}
