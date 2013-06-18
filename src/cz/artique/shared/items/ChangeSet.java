package cz.artique.shared.items;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

/**
 * Lists changes to be performed onto stored {@link UserItem}s. ChangeSet may
 * add or remove {@link Label}s or change read status.
 * 
 * <p>
 * There is mechanism which cancels opposite changes: if user ads label l1 and
 * immediately removes it, the change is discarded. The same applies to read
 * state.
 * 
 * @author Adam Juraszek
 * 
 */
public class ChangeSet implements Serializable {
	private static final long serialVersionUID = 1L;
	private Key userItem;
	private Set<Key> labelsAdded = new HashSet<Key>();
	private Set<Key> labelsRemoved = new HashSet<Key>();
	private Boolean readState = null;
	private boolean originalRead;

	/**
	 * Constructor for deserialization.
	 */
	public ChangeSet() {}

	/**
	 * Constructs the {@link ChangeSet} and sets key of {@link UserItem} the
	 * change applies to and original read state.
	 * 
	 * @param userItem
	 *            {@link UserItem} the change applies to
	 * @param originalRead
	 *            original read state
	 */
	public ChangeSet(Key userItem, boolean originalRead) {
		this.setUserItem(userItem);
		this.setOriginalRead(originalRead);
	}

	/**
	 * Adds label to list of labels to add or removes the label from lists of
	 * labels to remove.
	 * 
	 * @param label
	 *            label to add
	 * @return whether change set has been modified
	 */
	public boolean addLabel(Key label) {
		if (!getLabelsRemoved().remove(label)) {
			getLabelsAdded().add(label);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Adds label to list of labels to remove or removes the label from lists of
	 * labels to add.
	 * 
	 * @param label
	 *            label to remove
	 * @return whether change set has been modified
	 */
	public boolean removeLabel(Key label) {
		if (!getLabelsAdded().remove(label)) {
			getLabelsRemoved().add(label);
			return true;
		}
		return false;
	}

	/**
	 * @return {@link UserItem} the change applies to
	 */
	public Key getUserItem() {
		return userItem;
	}

	/**
	 * @return set of labels to add
	 */
	public Set<Key> getLabelsAdded() {
		return labelsAdded;
	}

	/**
	 * @return set of labels to remove
	 */
	public Set<Key> getLabelsRemoved() {
		return labelsRemoved;
	}

	/**
	 * @return new read state; null if not changed
	 */
	public Boolean getReadState() {
		return readState;
	}

	/**
	 * Sets read state to new read state or null to reset to original value.
	 * 
	 * @param readState
	 *            new read state
	 * @return whether change set has been modified
	 */
	public boolean setRead(Boolean readState) {
		if (readState == null || readState == isOriginalRead()) {
			this.readState = null;
			return false;
		} else {
			this.readState = readState;
			return true;
		}
	}

	/**
	 * {@link ChangeSet} is empty if there are no labels to add or remove and
	 * read state has not been changed.
	 * 
	 * @return whether change set is empty
	 */
	public boolean isEmpty() {
		return readState == null && getLabelsAdded().isEmpty()
			&& getLabelsRemoved().isEmpty();
	}

	/**
	 * @param userItem
	 *            {@link UserItem} the change applies to
	 */
	public void setUserItem(Key userItem) {
		this.userItem = userItem;
	}

	/**
	 * @param labelsAdded
	 *            set of labels to add
	 */
	public void setLabelsAdded(Set<Key> labelsAdded) {
		this.labelsAdded = labelsAdded;
	}

	/**
	 * @param labelsRemoved
	 *            set of labels to remove
	 */
	public void setLabelsRemoved(Set<Key> labelsRemoved) {
		this.labelsRemoved = labelsRemoved;
	}

	/**
	 * @return original read state
	 */
	public boolean isOriginalRead() {
		return originalRead;
	}

	/**
	 * @param originalRead
	 *            original read state
	 */
	public void setOriginalRead(boolean originalRead) {
		this.originalRead = originalRead;
	}

	/**
	 * @param readState
	 *            new read state
	 */
	public void setReadState(Boolean readState) {
		this.readState = readState;
	}
}
