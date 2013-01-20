package cz.artique.client.artiqueItems;

import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.event.shared.GwtEvent;

import cz.artique.shared.model.item.UserItem;

public class ModifiedEvent extends GwtEvent<ModifiedHandler> {

	private static final Type<ModifiedHandler> TYPE =
		new Type<ModifiedHandler>();
	private final Map<Key, UserItem> modified;

	public static Type<ModifiedHandler> getType() {
		return TYPE;
	}

	public ModifiedEvent(Map<Key, UserItem> modified) {
		this.modified = modified;
	}

	@Override
	public final Type<ModifiedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifiedHandler handler) {
		handler.onModified(this);
	}

	public Map<Key, UserItem> getModified() {
		return modified;
	}

}
