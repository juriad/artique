package cz.artique.client.labels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryManager;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.label.ListFilter;

public class ClickableLabelWidget extends LabelWidget {

	public static final ClickableArtiqueLabelFactory FACTORY =
		new ClickableArtiqueLabelFactory();

	public static class ClickableArtiqueLabelFactory
			implements LabelWidgetFactory {
		public LabelWidget createWidget(Label l) {
			return new ClickableLabelWidget(l);
		}
	}

	private final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

	public ClickableLabelWidget(final Label label) {
		super(label);

		if (!LabelType.SYSTEM.equals(label.getLabelType())) {
			Filter filter = CachingHistoryUtils.UTILS.getFilterForLabel(label);
			String serialized =
				CachingHistoryUtils.UTILS.serializeListFilter(filter);
			nameLabel.setHref("#" + serialized);
		}

		nameLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()
					&& impl.handleAsClick(Event.as(event.getNativeEvent()))) {
					Filter filter =
						CachingHistoryUtils.UTILS.getFilterForLabel(label);
					ListFilter listFilter =
						HistoryManager.HISTORY.getBaseListFilter();
					listFilter.setFilterObject(filter);
					String serialized =
						CachingHistoryUtils.UTILS.serializeListFilter(filter);
					HistoryManager.HISTORY
						.setListFilter(listFilter, serialized);
				}
				event.preventDefault();
			}
		});
	}

}
