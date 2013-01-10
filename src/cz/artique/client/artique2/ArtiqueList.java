package cz.artique.client.artique2;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import cz.artique.client.listing2.RowWidgetFactory;
import cz.artique.client.listing2.WidgetList;
import cz.artique.shared.utils.HasKey;

public class ArtiqueList<E extends HasKey<K>, K> extends WidgetList<E, K> {

	public ArtiqueList(RowWidgetFactory<E, K> factory) {
		super(factory);

		scrollPanel.setWidth("100%");
		scrollPanel.setHeight(Window.getClientHeight() + "px");
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
				scrollPanel.setHeight(height + "px");
			}
		});
	}
}
