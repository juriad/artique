package cz.artique.client.listing;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;
import static com.google.gwt.dom.client.BrowserEvents.KEYUP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public abstract class InfiniteListCell<E> extends AbstractCell<E>
		implements Handler {

	interface OuterTemplate extends SafeHtmlTemplates {
		// @formatter:off
		@Template("<div class='header'>"
			+ "{0}"
			+ "</div>" 
			+ "<div class='content {2}'>" 
			+ "{1}" 
			+ "</div>")
		// @formatter:on
		SafeHtml cellItem(SafeHtml header, SafeHtml content, String contentClass);
	}

	private static OuterTemplate outerTemplate;

	protected SingleSelectionModel<E> selectionModel;
	private E selected;
	private boolean expanded;

	protected CellList<E> cellList;

	private Map<E, Integer> indices;

	public InfiniteListCell(String... consumedEvents) {
		super(addMyEvents(consumedEvents));
		if (outerTemplate == null) {
			outerTemplate = GWT.create(OuterTemplate.class);
		}
		indices = new HashMap<E, Integer>();
	}

	private static Set<String> addMyEvents(String[] consumedEvents) {
		Set<String> events = new HashSet<String>();
		if (consumedEvents != null && consumedEvents.length > 0) {
			for (String s : consumedEvents) {
				events.add(s);
			}
		}
		events.add(CLICK);
		events.add(KEYUP);
		events.add(KEYDOWN);
		return events;
	}

	/**
	 * Must be set immediately after constructing object
	 * 
	 * @param selectionModel
	 */
	public void setModelAndList(SingleSelectionModel<E> selectionModel,
			CellList<E> cellList) {
		this.cellList = cellList;
		this.selectionModel = selectionModel;
		selectionModel.addSelectionChangeHandler(this);
	}

	public void onSelectionChange(SelectionChangeEvent event) {
		E newSel = selectionModel.getSelectedObject();
		if (newSel == selected) {
			return; // did not changed
		}
		if (selected != null) {
			collapse(selected);
		}
		if (newSel != null) {
			expand(newSel);
		}
		selected = newSel;
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			E value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		SafeHtml header = renderHeader(value);
		if (value.equals(selected) && expanded) {
			// expanded
			SafeHtml content = renderContent(value);
			sb.append(outerTemplate.cellItem(header, content, "hasContent"));
		} else {
			// collapsed
			sb.append(outerTemplate.cellItem(header, null, ""));
		}
	}

	protected int getIndex(E e) {
		Integer index = indices.get(e);
		if (index != null) {
			E e2 = cellList.getVisibleItem(index);
			if (e.equals(e2)) {
				return index;
			} else {
				// clear indices
				indices.clear();
			}
		}
		// not found or cleared
		List<E> visibleItems = cellList.getVisibleItems();
		for (int i = 0; i < visibleItems.size(); i++) {
			if (visibleItems.get(i).equals(e)) {
				indices.put(e, i);
				return i;
			}
		}
		return -1;
	}

	protected boolean hasClassName(Element e, String className) {
		className = className.trim();
		// Get the current style string.
		String oldClassName = e.getClassName();
		int idx = oldClassName.indexOf(className);

		// Calculate matching index.
		while (idx != -1) {
			if (idx == 0 || oldClassName.charAt(idx - 1) == ' ') {
				int last = idx + className.length();
				int lastPos = oldClassName.length();
				if ((last == lastPos)
					|| ((last < lastPos) && (oldClassName.charAt(last) == ' '))) {
					break;
				}
			}
			idx = oldClassName.indexOf(className, idx + 1);
		}
		return idx > 0;
	}

	/**
	 * Only selected may be expanded
	 * 
	 * @param e
	 */
	protected void expand(E e) {
		int index = getIndex(e);
		Element rowElement = cellList.getRowElement(index);
		Element content = rowElement.getChild(1).cast();
		if (!hasClassName(content, "hasContent")) {
			content.setInnerSafeHtml(renderContent(e));
			content.addClassName("hasContent");
		}
		content.addClassName("expanded");
	}

	protected abstract SafeHtml renderContent(E e);

	protected abstract SafeHtml renderHeader(E e);

	protected void collapse(E e) {
		int index = getIndex(e);
		Element rowElement = cellList.getRowElement(index);
		Element content = rowElement.getChild(1).cast();
		content.removeClassName("expanded");
	}
}
