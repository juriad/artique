package cz.artique.client.leftPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Defines left panel which consists of options, sources, filters and messages.
 * 
 * @author Adam Juraszek
 * 
 */
public class LeftPanel extends StackLayoutPanel {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("LeftPanel.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	public LeftPanel(Unit unit) {
		super(unit);
		res.style().ensureInjected();
		setStylePrimaryName("leftPanel");

		addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				for (int i = 0; i < getWidgetCount(); i++) {
					getHeaderWidget(i).setStyleDependentName("selected",
						i == event.getSelectedItem());
				}
			}
		});
	}

	/**
	 * Force custom style.
	 * 
	 * @see com.google.gwt.user.client.ui.StackLayoutPanel#insert(com.google.gwt.user.client.ui.Widget,
	 *      com.google.gwt.user.client.ui.Widget, double, int)
	 */
	@Override
	public void insert(Widget child, Widget header, double headerSize,
			int beforeIndex) {
		super.insert(child, header, headerSize, beforeIndex);
		child.setStylePrimaryName("leftPanelContent");
		header.setStylePrimaryName("leftPanelHeader");
	}

	/**
	 * Show widget optionally without animation.
	 * 
	 * @param animate
	 *            whether animation shall be shown
	 * @param index
	 *            index of widget to be shown
	 */
	public void showWidget(boolean animate, int index) {
		int duration = getAnimationDuration();
		if (!animate) {
			setAnimationDuration(0);
		}
		super.showWidget(index);
		setAnimationDuration(duration);
	}
}
