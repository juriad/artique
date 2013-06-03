package cz.artique.client.listing;

import com.google.code.gwteyecandy.Tooltip;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import cz.artique.client.labels.LabelsBar;
import cz.artique.shared.model.item.UserItem;

public class UserItemRow extends RowWidget {

	public static final UserItemRowFactory FACTORY = new UserItemRowFactory();

	public static class UserItemRowFactory {
		public RowWidget createWidget(UserItem data) {
			return new UserItemRow(data);
		}
	}

	private LabelsBar labels;
	private FlowPanel header;

	private Label title;
	private Label content;

	public UserItemRow(UserItem data) {
		super(data);

		header = new FlowPanel();
		labels = new LabelsBar(data);
		header.add(labels);

		title = new Label(getValue().getItemObject().getTitle());
		title.setStylePrimaryName("rowTitle");
		header.add(title);
		setHeader(header);

		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleExpanded();
			}
		});

		content = new Label(getValue().getItemObject().getContent().getValue());
		setContent(content);

		Tooltip tt = new Tooltip();
		tt.setText(getValue().getItemObject().getContentType().toString());
		tt.attachTo(content);
	}

	private void toggleExpanded() {
		if (isExpanded()) {
			collapse();
		} else {
			expand();
		}
	}

	public void refresh() {
		// TODO Auto-generated method stub

	}
}
