package cz.artique.client.labels;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public abstract class LabelsBar<E extends HasName & HasKey<K>, K>
		extends Composite implements RemoveHandler {
	private static final String addLabelSign = "+";

	private Label addLabel;

	private List<E> selectedLabels;

	private final PanelWithMore<LabelWidget<E>> panel;

	protected final LabelsManager<E, K> manager;

	private LabelWidgetFactory<E> factory;

	public LabelsBar(final LabelsManager<E, K> manager,
			LabelWidgetFactory<E> factory, int maxSize) {
		this.manager = manager;
		this.factory = factory;
		panel = new PanelWithMore<LabelWidget<E>>(maxSize);
		addLabel = new Label(addLabelSign);
		panel.setExtraWidget(addLabel);
		addLabel.addClickHandler(new ClickHandler() {

			private List<E> allLabels;

			public void onClick(ClickEvent event) {
				MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
				allLabels = manager.getLabels();
				allLabels.removeAll(getSelectedLabels());
				for (E e : allLabels) {
					oracle.add(e.getName());
				}

				final SuggestBox box = new SuggestBox(oracle);
				panel.setExtraWidget(box);
				panel.setShowMoreButton(false);
				box.getValueBox().addBlurHandler(new BlurHandler() {

					public void onBlur(BlurEvent event) {
						String value = box.getValue().trim();
						if (!value.isEmpty()) {
							save(value);
						} else {
							cancel();
						}
					}
				});
				box.addKeyDownHandler(new KeyDownHandler() {

					public void onKeyDown(KeyDownEvent event) {
						switch (event.getNativeKeyCode()) {
						case KeyCodes.KEY_ENTER:
						case KeyCodes.KEY_TAB:
							String value = box.getValue().trim();
							if (!value.isEmpty()) {
								save(value);
							} else {
								cancel();
							}
							break;
						case KeyCodes.KEY_ESCAPE:
							cancel();
							break;
						}
					}
				});
				box.setFocus(true);
			}

			public void save(String value) {
				boolean added = false;
				for (E e : allLabels) {
					if (e.getName().equals(value)) {
						if (!getSelectedLabels().contains(e)) {
							labelAdded(e);
						}
						added = true;
						break;
					}
				}
				if (!added) {
					newLabelAdded(value);
				}
				panel.setExtraWidget(addLabel);
				panel.setShowMoreButton(true);
			}

			public void cancel() {
				panel.setExtraWidget(addLabel);
				panel.setShowMoreButton(true);
			}
		});
	}

	protected void addLabel(E label) {
		getSelectedLabels().add(label);
		LabelWidget<E> labelWidget = factory.createWidget(label);
		panel.add(labelWidget);
		labelWidget.addRemoveHandler(this);
	}

	protected void removeLabel(LabelWidget<E> labelWidget) {
		getSelectedLabels().remove(labelWidget.getLabel());
		panel.remove(labelWidget);
	}

	protected abstract void newLabelAdded(String name);

	protected abstract void labelAdded(E label);

	protected abstract void labelRemoved(LabelWidget<E> labelWidget);

	public void onRemove(RemoveEvent e) {
		if (e.getSource() instanceof LabelWidget) {
			@SuppressWarnings("unchecked")
			LabelWidget<E> toBeRemoved = (LabelWidget<E>) e.getSource();
			labelRemoved(toBeRemoved);
		}
	}

	public List<E> getSelectedLabels() {
		return selectedLabels;
	}
}
