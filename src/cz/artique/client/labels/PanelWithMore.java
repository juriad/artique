package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PanelWithMore<E extends ComparableWidget<E>> extends Composite {
	private static final String showLessSign = "«";
	private static final String showMoreSign = "»";

	private int maxSize;
	private boolean showMoreButton = true;
	private boolean expanded = false;

	private List<E> list;

	private FlowPanel head;
	private FlowPanel tail;
	private Label moreButton;
	private FlowPanel panel;
	private Widget extraWidget;

	public PanelWithMore(int maxSize) {
		this.maxSize = maxSize;
		list = new ArrayList<E>();
		panel = new FlowPanel();
		initWidget(panel);

		head = new FlowPanel();
		panel.add(head);
		moreButton = new Label(showMoreSign);
		moreButton.setVisible(false);
		moreButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (expanded) {
					moreButton.setText(showMoreSign);
					tail.setVisible(false);
				} else {
					moreButton.setText(showLessSign);
					tail.setVisible(true);
				}
				expanded = !expanded;
			}
		});
		panel.add(moreButton);
		tail = new FlowPanel();
		panel.add(tail);
	}

	public void initialFill(List<E> orderedList) {
		list = new ArrayList<E>(orderedList);
		head.clear();
		tail.clear();
		moreButton.setVisible(false);

		for (int i = 0; i < list.size(); i++) {
			if (i < maxSize) {
				head.add(list.get(i));
			} else {
				tail.add(list.get(i));
			}
		}
	}

	public void add(E w) {
		boolean added = false;
		for (int i = 0; i < list.size(); i++) {
			if (w.compareTo(list.get(i)) >= 0) {
				continue;
			}
			list.add(i, w);
			added = true;

			if (i < maxSize) {
				head.insert(w, i);
				if (getSize() > maxSize) {
					tail.insert(list.get(maxSize - 1), 0);
				}
			} else {
				tail.insert(w, i - maxSize);
			}
		}

		if (!added) {
			list.add(w);
			tail.add(w);
		}

		if (getSize() > maxSize) {
			if (!moreButton.isVisible() && isShowMoreButton()) {
				moreButton.setText(showMoreSign);
				moreButton.setVisible(true);
			}
		}
	}

	public void remove(E w) {
		int index = list.indexOf(w);
		if (index < 0) {
			return;
		}

		if (index >= maxSize) {
			tail.remove(index - maxSize);
		} else {
			head.remove(index);
			if (getSize() >= maxSize) {
				head.add(list.get(maxSize - 1));
			}
		}
	}

	public boolean isShowMoreButton() {
		return showMoreButton;
	}

	public void setShowMoreButton(boolean showMoreButton) {
		this.showMoreButton = showMoreButton;
		if (moreButton.isVisible()) {
			moreButton.setVisible(showMoreButton);
		}

		if (!expanded) {
			tail.setVisible(!showMoreButton);
		}
	}

	public int getSize() {
		return list.size();
	}

	public void clear() {
		list.clear();
		head.clear();
		tail.clear();
		moreButton.setVisible(false);
	}

	public void setExtraWidget(Widget w) {
		if (extraWidget != null) {
			panel.remove(extraWidget);
		}
		if (w != null) {
			panel.add(w);
		}
		extraWidget = w;
	}
}
