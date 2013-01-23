package cz.artique.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueLabel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.LabelsBar;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.labels.RemoveEvent;
import cz.artique.client.labels.RemoveHandler;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

public class TestSuggest extends Composite {

	class L implements HasKey<String>, HasName, Comparable<L> {

		private String name;

		public L(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getKey() {
			return name;
		}

		public int compareTo(L o) {
			return getName().compareTo(o.getName());
		}
	}

	public TestSuggest() {
		initWidget(new LabelsBar<L, String>(new LabelsManager<L, String>() {

			private Map<String, L> ls = new HashMap<String, L>();

			{
				add("aa");
				add("aaa");
				add("aab");
				add("b");
				add("baa");
			}

			private void add(String s) {
				L l = new L(s);
				ls.put(s, l);
			}

			public void refresh(AsyncCallback<Void> ping) {}

			public void setTimeout(int timeout) {}

			public int getTimeout() {
				return 0;
			}

			public void ready(AsyncCallback<Void> ping) {
				ping.onSuccess(null);
			}

			public boolean isReady() {
				return true;
			}

			public List<L> getLabels() {
				return new ArrayList<L>(ls.values());
			}

			public List<L> getUserDefinedLabels() {
				return new ArrayList<L>(ls.values());
			}

			public L getLabelByName(String name) {
				return ls.get(name);
			}

			public L getLabelByKey(String key) {
				return ls.get(key);
			}

			public void createNewLabel(String name, AsyncCallback<L> ping) {
				add(name);
				ping.onSuccess(getLabelByKey(name));
			}

			public List<L> getSortedList(List<String> keys) {
				List<L> lss = new ArrayList<L>();
				for (String s : keys) {
					lss.add(getLabelByKey(s));
				}
				Collections.sort(lss);
				return lss;
			}
		}, new LabelWidgetFactory<L>() {

			public LabelWidget<L> createWidget(final L l) {
				return new LabelWidget<L>() {

					private Label la = new Label(l.getName());

					{
						final LabelWidget<L> that = this;
						la.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								if (!enabled) {
									return;
								}
								if (rh != null) {
									RemoveEvent removeEvent =
										new RemoveEvent() {
											{
												setSource(that); // funguje
												// setSource(LabelWidget.this);
												// zakomentované nefunguje:
												// No enclosing instance of the
												// type LabelWidget<E> is
												// accessible in scope
											}
										};
									rh.onRemove(removeEvent);
								}
							}
						});
					}

					private RemoveHandler rh = null;
					private boolean enabled = true;

					public HandlerRegistration addRemoveHandler(
							RemoveHandler handler) {
						rh = handler;
						return new HandlerRegistration() {

							public void removeHandler() {
								rh = null;
							}
						};
					}

					public int compareTo(LabelWidget<L> o) {
						return l.compareTo(o.getLabel());
					}

					public Widget asWidget() {
						return la;
					}

					public L getLabel() {
						return l;
					}

					public boolean isEnabled() {
						return enabled;
					}

					public void setEnabled(boolean enabled) {
						this.enabled = enabled;
					}
				};
			}
		}, new SuggesionLabelFactory<L>() {

			public ValueLabel<L> createLabel() {
				return new ValueLabel<L>(new AbstractRenderer<L>() {

					public String render(L object) {
						return object.getName();
					}
				});
			}
		}, 3) {

			@Override
			protected void newLabelAdded(String name) {
				manager.createNewLabel(name, new AsyncCallback<L>() {

					public void onFailure(Throwable caught) {}

					public void onSuccess(L result) {
						addLabel(result);
					}
				});
			}

			@Override
			protected void labelAdded(L label) {
				addLabel(label);
			}

			@Override
			protected void labelRemoved(LabelWidget<L> labelWidget) {
				removeLabel(labelWidget);
			}
		});
	}

}
