package cz.artique.client.listing2;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.listing2.ExpandCollapseEvent.ExpandCollapseType;
import cz.artique.shared.utils.HasKey;

public abstract class AbstractRowWidget<E extends HasKey<K>, K>
		extends Composite implements RowWidget<E, K> {

	private E dataShown;

	private E dataNew;

	private boolean newDataExists;

	private boolean expanded;

	private Widget header;

	private Widget content;

	private VerticalPanel panel;

	public AbstractRowWidget(E data) {
		this.dataShown = this.dataNew = data;

		panel = new VerticalPanel();
		initWidget(panel);
		this.setStylePrimaryName("row");
	}

	protected void setContent(Widget content) {
		if (this.content != null) {
			panel.remove(this.content);
		}
		this.content = content;
		panel.add(content);
		content.setVisible(false);
		content.setStylePrimaryName("row-content");
		collapse();
	}

	protected void setHeader(Widget header) {
		if (this.header != null) {
			panel.remove(this.header);
		}
		this.header = header;
		header.setStylePrimaryName("row-header");
		panel.insert(header, 0);
	}

	protected abstract void newDataSet();

	public void setNewData(E e) {
		dataNew = e;
		newDataExists = true;
		newDataSet();
	}

	public E getData(boolean shown) {
		return shown ? dataShown : dataNew;
	}

	public K getKey() {
		return dataShown.getKey();
	}

	public void expand() {
		if (isExpanded())
			return;
		expanded = true;
		if (content != null) {
			content.setVisible(true);
		}
		setStyleDependentName("expanded", true);
		fireEvent(new ExpandCollapseEvent(ExpandCollapseType.EXPAND));
	}

	public void collapse() {
		if (!isExpanded())
			return;
		expanded = false;
		if (content != null) {
			content.setVisible(false);
		}
		setStyleDependentName("expanded", false);
		fireEvent(new ExpandCollapseEvent(ExpandCollapseType.COLLAPSE));
	}

	public boolean isExpanded() {
		return expanded;
	}

	protected boolean isThereNewData() {
		return newDataExists;
	}

	protected E consumeNewData() {
		newDataExists = false;
		dataShown = dataNew;
		return dataNew;
	}

	public HandlerRegistration addExpandCollapseHandler(
			ExpandCollapseHandler handler) {
		return addHandler(handler, ExpandCollapseEvent.getType());
	}
}
