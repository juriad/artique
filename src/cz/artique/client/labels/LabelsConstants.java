package cz.artique.client.labels;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface LabelsConstants extends ConstantsWithLookup {
	@DefaultStringValue("AND")
	String operatorAnd();

	@DefaultStringValue("OR")
	String operatorOr();
}
