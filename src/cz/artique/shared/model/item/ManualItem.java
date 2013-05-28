package cz.artique.shared.model.item;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class ManualItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public ManualItem() {}

	public ManualItem(Source source) {
		super(source);
	}

}
