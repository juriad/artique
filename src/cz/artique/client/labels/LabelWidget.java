package cz.artique.client.labels;

public interface LabelWidget<E>
		extends HasRemoveHandlers, ComparableWidget<LabelWidget<E>> {
	E getLabel();
}
