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

	protected ClickHandler HIDE = new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide();
		}
	};

	private OnShowAction<T> onShow;

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

	public Button addButton(Button button) {
		button.addStyleName("dialogButton");
		buttons.add(button);
		return button;
	}

	public Button addButton(String name, ClickHandler handler) {
		Button button = new Button(name, handler);
		return addButton(button);
	}

	@Override
	public void setWidget(Widget widget) {
		content.clear();
		content.add(widget);
	}

	public void setShowAction(OnShowAction<T> onShow) {
		this.onShow = onShow;
	}

	public void showDialog() {
		showDialog(null);
	}

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
