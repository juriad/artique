package cz.artique.client.artiqueLabels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.CachingHistoryUtils;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;

public class ClickableArtiqueLabel extends ArtiqueLabelWidget {
	
	public static final ClickableArtiqueLabelFactory FACTORY =
			new ClickableArtiqueLabelFactory(false);

		public static final ClickableArtiqueLabelFactory REMOVABLE_FACTORY =
			new ClickableArtiqueLabelFactory(true);

		public static class ClickableArtiqueLabelFactory
				implements LabelWidgetFactory<Label> {

			private final boolean removable;

			public ClickableArtiqueLabelFactory(boolean removable) {
				this.removable = removable;
			}

			public ArtiqueLabelWidget createWidget(Label l) {
				return new ArtiqueLabelWidget(l, removable);
			}
		}

	public ClickableArtiqueLabel(final Label label, boolean removable) {
		super(label, removable);
		
		Filter filter = CachingHistoryUtils.UTILS.getFilterForLabel(label);
		String serialized =
			CachingHistoryUtils.UTILS.serializeListFilter(filter);
		nameLabel.setHref("#" + serialized);
		
		nameLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					Filter filter =
						CachingHistoryUtils.UTILS.getFilterForLabel(label);
					ListFilter listFilter =
						ArtiqueHistory.HISTORY.getBaseListFilter();
					listFilter.setFilterObject(filter);
					String serialized =
						CachingHistoryUtils.UTILS.serializeListFilter(filter);
					ArtiqueHistory.HISTORY
						.addListFilter(listFilter, serialized);
				}
				event.preventDefault();
			}
		});
	}

}
