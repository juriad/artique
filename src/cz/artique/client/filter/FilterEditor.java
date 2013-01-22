package cz.artique.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FilterEditor extends Composite {
	private static FilterEditorUiBinder uiBinder = GWT.create(FilterEditorUiBinder.class);

	interface FilterEditorUiBinder extends UiBinder<Widget, FilterEditor> {}
}
