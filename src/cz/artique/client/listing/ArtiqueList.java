package cz.artique.client.listing;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import cz.artique.client.listing.ScrollEndEvent.ScrollEndType;
import cz.artique.client.manager.Managers;
import cz.artique.client.shortcuts.ShortcutEvent;
import cz.artique.client.shortcuts.ShortcutHandler;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class ArtiqueList extends InfiniteList {

	private class MyShortcutHandler implements ShortcutHandler {
		public void onShortcut(ShortcutEvent e) {
			switch (e.getShortcut().getType()) {
			case ACTION:
				switch (e.getShortcut().getAction()) {
				case ADD_LABEL: {
					RowWidget selectedRowWidget = getSelectedRowWidget();
					if (selectedRowWidget != null) {
						selectedRowWidget.openAddLabel();
					}
				}
					break;
				case MARK_READ_ALL: {
					int selectedIndex = getSelectedIndex();
					for (int i = 0; i <= selectedIndex; i++) {
						RowWidget row = getRow(i);
						UserItem value = row.getValue();
						boolean read = value.isRead();
						if (!read) {
							value.setRead(true);
							row.setValue(value);
							Managers.ITEMS_MANAGER.readSet(value, true, null);
						}
					}
				}
					break;
				case NEXT_ITEM: {
					int selectedIndex1 =
						Math.min(getSelectedIndex() + 1, getRowCount() - 1);
					setSelectedIndex(selectedIndex1, false);
				}
					break;
				case OPEN_ORIGINAL: {
					RowWidget selectedRowWidget = getSelectedRowWidget();
					if (selectedRowWidget != null) {
						selectedRowWidget.openOriginal();
					}
				}
					break;
				case PREVIOUS_ITEM: {
					int selectedIndex2 = Math.max(getSelectedIndex() - 1, 0);
					setSelectedIndex(selectedIndex2, false);
				}
					break;
				case TOGGLE_COLLAPSED: {
					RowWidget selectedRowWidget = getSelectedRowWidget();
					if (selectedRowWidget != null) {
						selectedRowWidget.toggleExpanded();
					}
				}
					break;
				default:
					break;
				}
				break;
			case LABEL: {
				RowWidget selectedRowWidget = getSelectedRowWidget();
				if (selectedRowWidget != null) {
					UserItem value = selectedRowWidget.getValue();
					List<Key> labels = value.getLabels();
					if (labels == null) {
						labels = new ArrayList<Key>();
					}
					Key referenced = e.getShortcut().getReferenced();
					Label label = e.getShortcut().getReferencedLabel();
					if (labels.contains(referenced)) {
						// remove:
						Managers.ITEMS_MANAGER.labelRemoved(value, label, null);
					} else {
						// add:
						Managers.ITEMS_MANAGER.labelAdded(value, label, null);
					}
					selectedRowWidget.setValue(value);
				}
			}
				break;
			default:
				break;
			}
		}
	}

	private class SelectionHandler implements Handler {
		public void onSelectionChange(SelectionChangeEvent event) {
			RowWidget selectedRowWidget = getSelectedRowWidget();
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

	private class EndHandler implements ScrollEndHandler {

		public void onScrollEnd(ScrollEndEvent event) {
			if (resetScrollToTop) {
				resetScrollToTop = false;
				scrollPanel.scrollToTop();
				return;
			}
			if (ScrollEndType.NEAR_BOTTOM.equals(event.getScrollEndType())
				|| ScrollEndType.BOTTOM.equals(event.getScrollEndType())) {
				if (!isEndReached()) {
					GWT.log("near bottom or bottom");
					getProvider().fetch(-1);
				}
			}
		}
	}

	private class MyScrollHandler implements ScrollHandler {
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
	}

	public ArtiqueList() {
		scrollPanel.setWidth("100%");
		scrollPanel.setHeight(Window.getClientHeight() + "px");
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				scrollPanel.setHeight(event.getHeight() + "px");
			}
		});

		scrollPanel.addScrollHandler(new MyScrollHandler());
		this.addSelectionChangeHandler(new SelectionHandler());
		this.addScrollEndHandler(new EndHandler());
		Managers.SHORTCUTS_MANAGER.addShortcutHandler(new MyShortcutHandler());
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
