package cz.artique.client.labels;

import com.google.gwt.user.client.ui.InlineLabel;

public class AddLabelButtonFactory {

	public static final AddLabelButtonFactory FACTORY =
		new AddLabelButtonFactory();
	
	protected static final String addLabelSign = "+";

	public InlineLabel createAddLabel() {
		InlineLabel addButton = new InlineLabel(addLabelSign);

		return addButton;
	}
}
