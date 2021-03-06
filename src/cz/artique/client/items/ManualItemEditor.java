package cz.artique.client.items;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.manager.Managers;
import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.source.UserSource;

/**
 * Editor shown inside {@link ManualItemDialog}; defines layout and control.
 * 
 * @author Adam Juraszek
 * 
 */
public class ManualItemEditor extends Composite implements HasValue<UserItem> {
	private static ManualItemEditorUiBinder uiBinder = GWT
		.create(ManualItemEditorUiBinder.class);

	interface ManualItemEditorUiBinder
			extends UiBinder<Widget, ManualItemEditor> {}

	@UiField
	Grid grid;

	@UiField
	TextBox title;

	@UiField
	TextBox url;

	@UiField
	TextArea content;

	@UiField
	ManualItemLabelsBar labels;

	public ManualItemEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserItem> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Returns {@link UserItem} containing {@link ManualItem} which user has
	 * filled in the form.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	public UserItem getValue() {
		ManualItem item = new ManualItem();
		item.setTitle(title.getValue());
		item.setUrl(new Link(url.getValue()));

		if (!content.getValue().trim().isEmpty()) {
			item.setContent(new Text(content.getValue().trim()));
		}
		item.setContentType(ContentType.PLAIN_TEXT);

		UserItem userItem = new UserItem(item, null);

		List<Label> allLabels = labels.getLabels();
		List<Key> labelKeys = new ArrayList<Key>();
		for (Label l : allLabels) {
			labelKeys.add(l.getKey());
		}
		userItem.setLabels(labelKeys);
		userItem.setItemObject(item);
		return userItem;
	}

	/**
	 * Prepares blank editor.
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue(UserItem value) {
		title.setValue("");
		url.setValue("");
		content.setValue("");
		UserSource manualSource = Managers.SOURCES_MANAGER.getManualSource();
		labels.setNewData(manualSource.getDefaultLabels());
	}

	public void setValue(UserItem value, boolean fireEvents) {
		setValue(value);
	}
}
