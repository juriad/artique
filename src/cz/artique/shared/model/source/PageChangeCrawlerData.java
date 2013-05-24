package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.source.CrawlerData;

@Model(schemaVersion = 1)
public class PageChangeCrawlerData extends CrawlerData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Text content;

	public Text getContent() {
		return content;
	}

	public void setContent(Text content) {
		this.content = content;
	}

}
