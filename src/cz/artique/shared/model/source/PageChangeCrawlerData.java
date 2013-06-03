package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Text;

@Model(schemaVersion = 1)
public class PageChangeCrawlerData extends CrawlerData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(unindexed = true)
	private Text content;

	public Text getContent() {
		return content;
	}

	public void setContent(Text content) {
		this.content = content;
	}

}
