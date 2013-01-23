package cz.artique.client.artiqueFilter;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.datepicker.client.DateBox;

public class OptionalDateBox extends Composite implements HasEnabled {

	private final FlowPanel panel;
	private final DateBox dateBox;
	private final CheckBox checkBox;

	private boolean enabled = true;

	public OptionalDateBox() {
		panel = new FlowPanel();
		initWidget(panel);
		checkBox = new CheckBox();
		panel.add(checkBox);

		dateBox = new DateBox();
		panel.add(dateBox);

		checkBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dateBox.setEnabled(checkBox.getValue());
			}
		});
	}

	public void setLabel(String msg) {
		checkBox.setText(msg);
	}

	public void setDate(Date date) {
		checkBox.setValue(date == null);
		dateBox.setEnabled(date != null);
		dateBox.setValue(date);
	}

	public Date getDate() {
		if (dateBox.isEnabled()) {
			return dateBox.getValue();
		}
		return null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		checkBox.setEnabled(enabled);
		dateBox.setEnabled(enabled);
	}
}
