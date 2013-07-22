package cz.artique.client.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;

public class ContentsPanel<E extends Widget & HasName & HasClickHandlers>
		extends Composite {

	public interface ContentButtonFactory<E extends Widget & HasName & HasClickHandlers> {
		E createButton(String name);
	}

	private final FlowPanel buttonsPanel;
	private final HTMLPanel content;

	private Map<String, SafeHtml> contents;
	private Map<String, E> buttons;
	private final ContentButtonFactory<E> factory;

	private ClickHandler handler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (event.getSource() instanceof HasName) {
				String name = ((HasName) event.getSource()).getName();
				setContent(name);
			}
		}
	};

	public ContentsPanel(ContentButtonFactory<E> factory) {
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

	public void addContent(String name, Element element) {
		addContent(name,
			SafeHtmlUtils.fromTrustedString(element.getInnerHTML()));
	}

	public void addContent(String name, String string) {
		addContent(name, SafeHtmlUtils.fromString(string));
	}

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

	public void setContent(String name) {
		String asString = contents.get(name).asString();
		content.getElement().setInnerHTML(asString);
		for (String n : buttons.keySet()) {
			E button = buttons.get(n);
			button.setStyleDependentName("selected", n.equals(name));
		}
	}
}
