package cz.artique.shared.items;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

public class ChangeSet implements Serializable {
	private static final long serialVersionUID = 1L;
	private Key userItem;
	private Set<Key> labelsAdded = new HashSet<Key>();
	private Set<Key> labelsRemoved = new HashSet<Key>();
	private Boolean readState = null;
	private boolean originalRead;

	public ChangeSet() {}

	public ChangeSet(Key userItem, boolean originalRead) {
		this.setUserItem(userItem);
		this.setOriginalRead(originalRead);
	}

	public boolean addLabel(Key label) {
		if (!getLabelsRemoved().remove(label)) {
			getLabelsAdded().add(label);
			return true;
		}
		return false;
	}

	public boolean removeLabel(Key label) {
		if (!getLabelsAdded().remove(label)) {
			getLabelsRemoved().add(label);
			return true;
		}
		return false;
	}

	public Key getUserItem() {
		return userItem;
	}

	public Set<Key> getLabelsAdded() {
		return labelsAdded;
	}

	public Set<Key> getLabelsRemoved() {
		return labelsRemoved;
	}

	public Boolean getReadState() {
		return readState;
	}

	public boolean setRead(Boolean readState) {
		if (readState == null || readState == isOriginalRead()) {
			this.readState = null;
			return false;
		} else {
			this.readState = readState;
			return true;
		}
	}

	public boolean isEmpty() {
		return readState == null && getLabelsAdded().isEmpty()
			&& getLabelsRemoved().isEmpty();
	}

	public void setUserItem(Key userItem) {
		this.userItem = userItem;
	}

	public void setLabelsAdded(Set<Key> labelsAdded) {
		this.labelsAdded = labelsAdded;
	}

	public void setLabelsRemoved(Set<Key> labelsRemoved) {
		this.labelsRemoved = labelsRemoved;
	}

	public boolean isOriginalRead() {
		return originalRead;
	}

	public void setOriginalRead(boolean originalRead) {
		this.originalRead = originalRead;
	}

	public void setReadState(Boolean readState) {
		this.readState = readState;
	}
}
