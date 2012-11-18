package cz.artique.shared.model.item;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class LinkItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public LinkItem() {}

	public LinkItem(Source source) {
		super(source);
	}
}
