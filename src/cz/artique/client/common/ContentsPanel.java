package cz.artique.client.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extends Simple {@link HTMLPanel}; can switch between several contents by
 * clicking on buttons. It might be similar to {@link TabLayoutPanel}.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of button widget
 */
public class ContentsPanel<E extends Widget & HasName & HasClickHandlers>
		extends Composite {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("ContentsPanel.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	public interface ContentButtonFactory<E extends Widget & HasName & HasClickHandlers> {
		E createButton(String name);
	}

	private final FlowPanel buttonsPanel;
	private final HTMLPanel content;

	private Map<String, SafeHtml> contents;
	private Map<String, E> buttons;
	private final ContentButtonFactory<E> factory;

	/**
	 * Sets content by name of click event source.
	 */
	private ClickHandler handler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (event.getSource() instanceof HasName) {
				String name = ((HasName) event.getSource()).getName();
				setContent(name);
			}
		}
	};

	/**
	 * @param factory
	 *            factory of switch buttons
	 */
	public ContentsPanel(ContentButtonFactory<E> factory) {
		res.style().ensureInjected();
		this.factory = factory;
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		setStylePrimaryName("contentsPanel");

		buttonsPanel = new FlowPanel();
		buttonsPanel.setStylePrimaryName("contentsPanelButtons");
		panel.add(buttonsPanel);

		content = new HTMLPanel("");
		content.setStylePrimaryName("contentsPanelContent");
		panel.add(content);

		contents = new HashMap<String, SafeHtml>();
		buttons = new HashMap<String, E>();
	}

	/**
	 * Add a new content by its name and DOM element.
	 * The content will be set to innerHTML of the element.
	 * 
	 * @param name
	 *            name of content
	 * @param element
	 *            DOM element
	 */
	public void addContent(String name, Element element) {
		addContent(name,
			SafeHtmlUtils.fromTrustedString(element.getInnerHTML()));
	}

	/**
	 * Adds a new content by its name and unescaped string.
	 * 
	 * @param name
	 *            name of content
	 * @param string
	 *            unescaped string content
	 */
	public void addContent(String name, String string) {
		addContent(name, SafeHtmlUtils.fromString(string));
	}

	/**
	 * Adds a new content by its name and {@link SafeHtml} representation.
	 * 
	 * @param name
	 *            name of content
	 * @param html
	 *            {@link SafeHtml} representation of content
	 */
	public void addContent(String name, SafeHtml html) {
		E button = factory.createButton(name);
		button.setStylePrimaryName("contentsPanelButton");
		button.addClickHandler(handler);
		button.setName(name);

		buttons.put(name, button);
		buttonsPanel.add(button);
		contents.put(name, html);

		if (contents.size() == 1) {
			setContent(name);
		}
	}

	/**
	 * Sets content which will be shown to the one represented by argument name.
	 * 
	 * @param name
	 *            name of set content
	 */
	public void setContent(String name) {
		String asString = contents.get(name).asString();
		content.getElement().setInnerHTML(asString);
		for (String n : buttons.keySet()) {
			E button = buttons.get(n);
			button.setStyleDependentName("selected", n.equals(name));
		}
	}
}
