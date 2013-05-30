package cz.artique.server.service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import cz.artique.shared.model.source.Source;

public class CheckService {
	private static final String queueName = "checkSources";

	public CheckService() {}

	public void check(Source source) {
		if (source.isEnabled() && !source.isEnqued()) {
			enque(source.getKey());
			source.setEnqued(true);
			
			SourceService ss = new SourceService();
			ss.saveSource(source);
		}
	}

	private void enque(Key sourceKey) {
		Queue queue = QueueFactory.getQueue(queueName);
		TaskOptions task =
			TaskOptions.Builder.withPayload(new CheckSourceTask(sourceKey));
		queue.add(task);
	}
}
