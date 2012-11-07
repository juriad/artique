package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class PageChangeItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Full diff of change
	 */
	private Text diff;

	/**
	 * Diff was created against page of this date
	 */
	private Date comparedTo;

	public PageChangeItem() {}

	public PageChangeItem(Source source) {
		super(source);
		setItemType(ItemType.PAGE_CHANGE);
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

}
