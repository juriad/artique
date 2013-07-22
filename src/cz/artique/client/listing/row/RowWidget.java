package cz.artique.client.listing.row;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.shared.model.item.UserItem;

public abstract class RowWidget extends Composite
		implements HasOpenHandlers<RowWidget>, HasCloseHandlers<RowWidget>,
		HasValue<UserItem> {

	private UserItem value;

	private boolean expanded;

	private Widget content;

	public RowWidget(UserItem value) {
		this.value = value;
	}

	protected void setContent(Widget content) {
		this.content = content;
		content.setVisible(false);
		collapse();
	}

	public void expand() {
		if (isExpanded())
			return;
		expanded = true;
		if (content != null) {
			content.setVisible(true);
		}
		setStyleDependentName("expanded", true);
		OpenEvent.fire(this, this);
	}

	public void collapse() {
		if (!isExpanded())
			return;
		expanded = false;
		if (content != null) {
			content.setVisible(false);
		}
		setStyleDependentName("expanded", false);
		CloseEvent.fire(this, this);
	}

	public void toggleExpanded() {
		if (isExpanded()) {
			collapse();
		} else {
			expand();
		}
	}

	public boolean isExpanded() {
		return expanded;
	}

	public HandlerRegistration addOpenHandler(OpenHandler<RowWidget> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	public HandlerRegistration addCloseHandler(CloseHandler<RowWidget> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	public UserItem getValue() {
		return value;
	}

	public void setValue(UserItem value) {
		setValue(value, true);
	}

	public void setValue(UserItem value, boolean fireEvents) {
		this.value = value;
		refresh();
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	public abstract void refresh();

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserItem> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Key getKey() {
		return getValue().getKey();
	}

	public abstract void openOriginal();

	public abstract void openAddLabel();
}
