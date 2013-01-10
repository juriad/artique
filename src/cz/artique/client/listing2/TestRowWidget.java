package cz.artique.client.listing2;

import com.google.code.gwteyecandy.Tooltip;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class TestRowWidget extends AbstractRowWidget<TestRowData, String> {

	public static final TestRowWidgetFactory factory =
		new TestRowWidgetFactory();

	public static class TestRowWidgetFactory
			implements RowWidgetFactory<TestRowData, String> {

		public RowWidget<TestRowData, String> createWidget(TestRowData data) {
			return new TestRowWidget(data);
		}

	}

	private Label title;
	private Label content;

	public TestRowWidget(TestRowData data) {
		super(data);

		title = new Label(getData(false).getTitle());
		setHeader(title);

		title.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleExpanded();
			}
		});

		content = new Label(getData(false).getContent());
		setContent(content);

		Tooltip tt = new Tooltip();
		tt.setText(getKey());
		tt.attachTo(content);
	}

	private void toggleExpanded() {
		if (isExpanded()) {
			collapse();
		} else {
			expand();
		}
	}

	@Override
	protected void newDataSet() {
		content.setText(consumeNewData().getContent());
	}

}
