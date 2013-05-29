package cz.artique.server.service;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;

public class BackupTask implements DeferredTask {

	private static final long serialVersionUID = 1L;

	private final Key userItemKey;
	private final Key backupLabelKey;

	public BackupTask(Key itemKey, Key backupLabelKey) {
		this.userItemKey = itemKey;
		this.backupLabelKey = backupLabelKey;
	}

	public void run() {
		ItemService is = new ItemService();
		UserItem userItem = is.getByKey(userItemKey);
		LabelService ls = new LabelService();
		Label backupLabel = ls.getLabelByKey(backupLabelKey);

		BackupService bs = new BackupService();
		BlobKey blobKey;
		try {
			blobKey = bs.backup(userItem, backupLabel.getBackupLevel());
			is.setBackup(userItemKey, blobKey);
		} catch (CrawlerException e) {
			throw new RuntimeException("Backuping item " + userItem.getKey()
				+ " failed.");
		}
	}
}
