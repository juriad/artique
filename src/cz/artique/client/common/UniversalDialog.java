package cz.artique.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Nicely styled dialog which has caption at the top and list of buttons at the
 * bottom.
 * 
 * @author Adam Juraszek
 * 
 * @param <T>
 *            type of parameter the {@link OnShowAction#onShow(Object)} is
 *            called with
 */
public class UniversalDialog<T> extends StopDialog {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("UniversalDialog.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	public interface OnShowAction<T> {
		boolean onShow(T param);
	}

	private FlowPanel buttons;
	private FlowPanel content;

	/**
	 * Predefined HIDE action; it is often used.
	 */
	protected ClickHandler HIDE = new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide();
		}
	};

	private OnShowAction<T> onShow;

	/**
	 * Constructs the dialog.
	 */
	public UniversalDialog() {
		super(true, true);
		res.style().ensureInjected();
		setStylePrimaryName("universalDialog");
		FlowPanel whole = new FlowPanel();

		content = new FlowPanel();
		content.setStylePrimaryName("dialogWidget");
		whole.add(content);

		buttons = new FlowPanel();
		buttons.setStylePrimaryName("dialogButtons");
		whole.add(buttons);

		super.setWidget(whole);
	}

	/**
	 * Adds a {@link Button} to the bottom.
	 * 
	 * @param button
	 *            button to add
	 * @return added button
	 */
	public Button addButton(Button button) {
		button.addStyleName("dialogButton");
		buttons.add(button);
		return button;
	}

	/**
	 * Creates a {@link Button} by name and sets a {@link ClickHandler}; adds it
	 * to the bottom.
	 * 
	 * @param name
	 *            name of button
	 * @param handler
	 *            click handler
	 * @return added button
	 */
	public Button addButton(String name, ClickHandler handler) {
		Button button = new Button(name, handler);
		return addButton(button);
	}

	/**
	 * Setting is delegated to content panel.
	 * 
	 * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#setWidget(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void setWidget(Widget widget) {
		content.clear();
		content.add(widget);
	}

	/**
	 * @param onShow
	 *            action to be performed whenever the dialog is to be shown
	 */
	public void setShowAction(OnShowAction<T> onShow) {
		this.onShow = onShow;
	}

	/**
	 * Show dialog with null parameter.
	 * 
	 * @see #showDialog(Object)
	 */
	public void showDialog() {
		showDialog(null);
	}

	/**
	 * Show dialog and call {@link OnShowAction#onShow(Object)} with param.
	 * 
	 * @param param
	 *            param to be passed to {@link OnShowAction#onShow(Object)}
	 */
	public void showDialog(T param) {
		if (onShow != null) {
			boolean result = onShow.onShow(param);
			if (result == false) {
				return;
			}
		}
		setWidth("100%");
		center();
	}
}
