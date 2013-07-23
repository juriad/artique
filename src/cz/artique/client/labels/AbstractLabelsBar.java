package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.common.AddButton;
import cz.artique.client.common.PanelWithMore;
import cz.artique.client.labels.suggestion.LabelSuggestion;
import cz.artique.client.labels.suggestion.LabelsPool;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.model.label.Label;

public abstract class AbstractLabelsBar extends Composite {

	class LabelCloseHandler implements CloseHandler<LabelWidget> {
		public void onClose(CloseEvent<LabelWidget> e) {
			LabelWidget toBeRemoved = e.getTarget();
			labelRemoved(toBeRemoved);
		}
	}

	class LabelOpenHandler implements OpenHandler<AbstractLabelsBar> {

		private LabelSuggestion box;

		private List<AsyncCallback<Void>> pingsWaitingForSuggestionClose =
			new ArrayList<AsyncCallback<Void>>();

		public void onOpen(OpenEvent<AbstractLabelsBar> _event) {
			box = new LabelSuggestion(pool, 20);
			panel.setExtraWidget(box);
			panel.setExpanded(true);

			box.addSelectionHandler(new SelectionHandler<SuggestionResult>() {
				public void onSelection(SelectionEvent<SuggestionResult> event) {
					SuggestionResult selectedItem = event.getSelectedItem();
					if (selectedItem.hasValue()) {
						if (selectedItem.isExisting()) {
							labelSelected(selectedItem.getExistingValue());
						} else {
							newLabelSelected(selectedItem.getNewValue());
						}
					}
					end();
				}
			});
			box.focus();
		}

		private void end() {
			panel.setExtraWidget(addLabel);
			panel.setExpanded(false);
			box = null;
			for (AsyncCallback<Void> p : pingsWaitingForSuggestionClose) {
				p.onSuccess(null);
			}
			pingsWaitingForSuggestionClose =
				new ArrayList<AsyncCallback<Void>>();
		}

		private boolean isSuggestionOpen() {
			return box != null;
		}

		public void onSuggestionClosed(AsyncCallback<Void> ping) {
			if (labelOpenHandler.isSuggestionOpen()) {
				labelOpenHandler.pingsWaitingForSuggestionClose.add(ping);
			} else {
				ping.onSuccess(null);
			}
		}
	}

	private final AddButton<AbstractLabelsBar> addLabel;

	private final PanelWithMore<LabelWidget> panel;

	private final CloseHandler<LabelWidget> labelCloseHandler =
		new LabelCloseHandler();

	private final LabelsPool pool;

	private List<Label> labels = new ArrayList<Label>();

	private LabelOpenHandler labelOpenHandler;

	public AbstractLabelsBar(final LabelsPool pool) {
		this.pool = pool;
		panel = new PanelWithMore<LabelWidget>();
		initWidget(panel);

		addStyleName("labelsBar");

		addLabel = AddButton.FACTORY.createWidget(this);
		panel.setExtraWidget(addLabel);
		labelOpenHandler = new LabelOpenHandler();
		addLabel.addOpenHandler(labelOpenHandler);
	}

	protected void onSuggestionClosed(AsyncCallback<Void> ping) {
		labelOpenHandler.onSuggestionClosed(ping);
	}

	protected abstract LabelWidget createWidget(Label label);

	public void addLabel(Label label) {
		LabelWidget labelWidget = createWidget(label);
		panel.insert(labelWidget);
		labelWidget.addCloseHandler(labelCloseHandler);
		labels.add(label);
	}

	public void removeLabel(Label label) {
		for (LabelWidget w : panel.getWidgets()) {
			if (w.getLabel().equals(label)) {
				panel.remove(w);
				break;
			}
		}
		labels.remove(label);
	}

	protected void removeLabel(LabelWidget widget) {
		panel.remove(widget);
		labels.remove(widget.getLabel());
	}

	protected abstract void newLabelSelected(String name);

	protected abstract void labelSelected(Label label);

	protected abstract void labelRemoved(LabelWidget labelWidget);

	public List<Label> getLabels() {
		return labels;
	}

	protected void removeAllLabels() {
		Widget extraWidget = panel.getExtraWidget();
		labels = new ArrayList<Label>();
		panel.clear();
		panel.setExtraWidget(extraWidget);
	}

	public void openSuggestion() {
		labelOpenHandler.onOpen(null);
	}
}
