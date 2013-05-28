package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class PageChangeItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Full diff of change
	 */
	@Attribute(unindexed = true)
	private Text diff;

	@Attribute(unindexed = true)
	private ContentType diffType;

	/**
	 * Diff was created against page of this date
	 */
	@Attribute(unindexed = true)
	private Date comparedTo;

	public PageChangeItem() {}

	public PageChangeItem(Source source) {
		super(source);
	}

	public Date getComparedTo() {
		return comparedTo;
	}

	public Text getDiff() {
		return diff;
	}

	public void setComparedTo(Date comparedTo) {
		this.comparedTo = comparedTo;
	}

	public void setDiff(Text diff) {
		this.diff = diff;
	}

	public ContentType getDiffType() {
		return diffType;
	}

	public void setDiffType(ContentType diffType) {
		this.diffType = diffType;
	}

}
