package cz.artique.server.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.shared.model.source.Source;

public class CheckService {
	private static final String queueName = "checkSources";

	public CheckService() {}

	public void check(Key key) {
		SourceMeta meta = SourceMeta.get();
		Source source = Datastore.get(meta, key);
		if (source.getParent() != null) {
			check(source.getParent());
		} else {
			// this is root source
			if (source.isEnabled() && !source.isEnqued()) {
				enque(key);
				source.setEnqued(true);
				Datastore.put(source);
			}
		}
	}

	private void enque(Key sourceKey) {
		Queue queue = QueueFactory.getQueue(queueName);
		TaskOptions task =
			TaskOptions.Builder.withPayload(new CheckSourceTask(sourceKey));
		queue.add(task);
	}
}
