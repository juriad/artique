package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.PageChangeCrawler;

/**
 * Represents auxiliary data used by {@link PageChangeCrawler} during check of
 * the {@link PageChangeSource}. {@link PageChangeCrawler} needs to save text
 * content of web-page in order to compare old and new version of content and
 * find differences.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class PageChangeCrawlerData extends CrawlerData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Text content of web-page restricted by region
	 */
	@Attribute(unindexed = true)
	private Text content;

	/**
	 * @return text content of web-page restricted by region
	 */
	public Text getContent() {
		return content;
	}

	/**
	 * @param content
	 *            text content of web-page restricted by region
	 */
	public void setContent(Text content) {
		this.content = content;
	}

}
