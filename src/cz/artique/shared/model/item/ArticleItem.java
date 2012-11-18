package cz.artique.shared.model.item;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class ArticleItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Author of article
	 */
	private String author;

	public ArticleItem() {}

	public ArticleItem(Source source) {
		super(source);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
