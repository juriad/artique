package cz.artique.client.labels;


public interface LabelWidgetFactory<E> {
	LabelWidget<E> createWidget(E l);
}
