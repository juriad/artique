package cz.artique.server.service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

/**
 * Task which is added to Task Queue in order to delay {@link UserItem} backup.
 * 
 * @author Adam Juraszek
 * 
 */
public class BackupTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private final Key userItemKey;
	private final Key backupLabelKey;

	/**
	 * Saves state-less key of {@link UserItem} to be backed up and key of
	 * {@link Label} which caused the backup.
	 * 
	 * @param itemKey
	 *            key of {@link UserItem} to back up
	 * @param backupLabelKey
	 *            key of {@link Label} which caused the backup
	 */
	public BackupTask(Key itemKey, Key backupLabelKey) {
		this.userItemKey = itemKey;
		this.backupLabelKey = backupLabelKey;
	}

	/**
	 * Gets {@link UserItem} and {@link Label} from database and delegates it to
	 * {@link BackupService#backup(UserItem, cz.artique.shared.model.label.BackupLevel)}
	 * .
	 * Finally it saves key of backup back to backed up {@link UserItem}.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ItemService is = new ItemService();
		UserItem userItem = is.getByKey(userItemKey);
		LabelService ls = new LabelService();
		Label backupLabel = ls.getLabelByKey(backupLabelKey);

		BackupService bs = new BackupService();
		try {
			bs.backup(userItem, backupLabel.getBackupLevel());
			is.setBackup(userItemKey, true);
		} catch (CrawlerException e) {
			throw new RuntimeException("Backuping item " + userItem.getKey()
				+ " failed.", e);
		}
	}
}
