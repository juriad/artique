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

/**
 * Abstract bar which contains list of {@link Label}s.
 * The bar also adds plus-sign button at the and, clicking which triggers new
 * suggestion of new {@link Label}.
 * 
 * @author Adam Juraszek
 * 
 */
public abstract class AbstractLabelsBar extends Composite {

	/**
	 * Remove label when cross button has been clicked.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	class LabelCloseHandler implements CloseHandler<LabelWidget> {
		public void onClose(CloseEvent<LabelWidget> e) {
			LabelWidget toBeRemoved = e.getTarget();
			labelRemoved(toBeRemoved);
		}
	}

	/**
	 * Show {@link LabelSuggestion} when {@link AddButton} has been clicked.
	 * Showing suggesting makes all request of new data be queued.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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

	/**
	 * Creates a new bar described by {@link LabelsPool}.
	 * 
	 * @param pool
	 *            {@link LabelsPool}
	 */
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

	/**
	 * Queue request if suggestion if open; call ping if suggestion is not
	 * shown.
	 * 
	 * @param ping
	 *            callback
	 */
	protected void onSuggestionClosed(AsyncCallback<Void> ping) {
		labelOpenHandler.onSuggestionClosed(ping);
	}

	/**
	 * Create {@link LabelWidget} for a {@link Label}.
	 * 
	 * @param label
	 *            {@link Label} to create widget for.
	 * @return {@link LabelWidget} for a {@link Label}.
	 */
	protected abstract LabelWidget createWidget(Label label);

	/**
	 * Called when a {@link Label} shall be added to the bar.
	 * 
	 * @param label
	 *            {@link Label} to add
	 */
	protected void addLabel(Label label) {
		LabelWidget labelWidget = createWidget(label);
		panel.insert(labelWidget);
		labelWidget.addCloseHandler(labelCloseHandler);
		labels.add(label);
	}

	/**
	 * Called when a {@link Label} shall be removed from the bar.
	 * 
	 * @param label
	 *            {@link Label} to remove
	 */
	protected void removeLabel(Label label) {
		for (LabelWidget w : panel.getWidgets()) {
			if (w.getLabel().equals(label)) {
				panel.remove(w);
				break;
			}
		}
		labels.remove(label);
	}

	/**
	 * Called when a {@link Label} shall be removed from the bar.
	 * This is optimized version of {@link #removeLabel(Label)} called when the
	 * widget is known.
	 * 
	 * @param widget
	 *            {@link LabelWidget} to remove
	 */
	protected void removeLabel(LabelWidget widget) {
		panel.remove(widget);
		labels.remove(widget.getLabel());
	}

	/**
	 * Called when user has chosen a non-existing {@link Label} in the
	 * {@link LabelSuggestion}.
	 * 
	 * @param name
	 *            name of chosen non-existing {@link Label}
	 */
	protected abstract void newLabelSelected(String name);

	/**
	 * Called when user has chosen an existing {@link Label} in the
	 * {@link LabelSuggestion}.
	 * 
	 * @param label
	 *            chosen existing {@link Label}
	 */
	protected abstract void labelSelected(Label label);

	/**
	 * Called when user has clicked the {@link Label} to be removed.
	 * 
	 * @param labelWidget
	 *            {@link LabelWidget} of {@link Label} to be removed
	 */
	protected abstract void labelRemoved(LabelWidget labelWidget);

	/**
	 * @return list of all currently assigned {@link Label}s.
	 */
	public List<Label> getLabels() {
		return labels;
	}

	/**
	 * Removes all currently assigned {@link Label}s leaving the bar empty.
	 */
	protected void removeAllLabels() {
		Widget extraWidget = panel.getExtraWidget();
		labels = new ArrayList<Label>();
		panel.clear();
		panel.setExtraWidget(extraWidget);
	}

	/**
	 * Forces the {@link LabelSuggestion} to open.
	 */
	public void openSuggestion() {
		labelOpenHandler.onOpen(null);
	}

	/**
	 * Delegate width request to {@link PanelWithMore}.
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
	 */
	@Override
	public void setWidth(String width) {
		panel.setWidth(width);
	}
}
