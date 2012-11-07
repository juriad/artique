package cz.artique.shared.model.hierarchy;

import java.util.List;
import java.util.Map;

public interface Hierarchy<E extends SupportsHierarchy> {

	void addChild(Hierarchy<E> child);

	String getHierarchy();

	void getChanges(Map<E, HierarchyChangeType> changes);

	List<Hierarchy<E>> getChildren();

	String getName();

	int getOrder();

	Hierarchy<E> getParent();

	void removeChild(Hierarchy<E> child);

	void setChange(HierarchyChangeType change);

	void setName(String name);

	void setOrder(int index);

	void setParent(Hierarchy<E> parent);

}
