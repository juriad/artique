package cz.artique.shared.model.label;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.model.label.Filter;

@Model(schemaVersion = 1)
public class Export extends Filter implements Serializable {

	private static final long serialVersionUID = 1L;

	public Export() {
		setOperator(Operator.OR);
		setParent(null);
	}
}
