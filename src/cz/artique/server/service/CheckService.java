package cz.artique.server.service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import cz.artique.shared.model.source.Source;

/**
 * Service which checks whether the {@link Source} can be checked and enques the
 * {@link CheckSourceTask}.
 * 
 * @author Adam Juraszek
 * 
 */
public class CheckService {
	private static final String queueName = "checkSources";

	public CheckService() {}

	/**
	 * Checks whether source can be checked and enques its check to Task Queue.
	 * 
	 * @param source
	 *            {@link Source} to be checked
	 */
	public void check(Source source) {
		if (source.isEnabled() && !source.isEnqued()) {
			enque(source.getKey());

			SourceService ss = new SourceService();
			ss.setEnqued(source, true);
		}
	}

	/**
	 * Creates a {@link CheckSourceTask} with {@link Source} key and enques it
	 * to Task Queuer.
	 * 
	 * @param sourceKey
	 *            key of {@link Source}
	 */
	private void enque(Key sourceKey) {
		Queue queue = QueueFactory.getQueue(queueName);
		TaskOptions task =
			TaskOptions.Builder.withPayload(new CheckSourceTask(sourceKey));
		queue.add(task);
	}
}
