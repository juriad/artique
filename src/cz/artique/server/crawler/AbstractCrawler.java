package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.artique.server.service.BackupService;
import cz.artique.server.service.ItemService;
import cz.artique.server.service.SourceService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.CheckStat;
import cz.artique.shared.model.source.UserSource;

public abstract class AbstractCrawler<E extends Source, F extends Item>
		extends Fetcher implements Crawler<E> {

	private final E source;

	private final BackupService backupService;

	protected AbstractCrawler(E source) {
		this.source = source;
		backupService = new BackupService();
	}

	protected void writeStat(int items) {
		CheckStat s = new CheckStat();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(items);
		SourceService ss = new SourceService();
		ss.addStat(s);
	}

	protected void writeStat(Throwable t) {
		CheckStat s = new CheckStat();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(0);
		s.setError(t.getLocalizedMessage());
		SourceService ss = new SourceService();
		ss.addStat(s);
	}

	protected List<F> createNonDuplicateItems(List<F> items) {
		List<F> items2 = new ArrayList<F>();
		for (F item : items) {
			F duplicate = createNonDuplicateItem(item);
			if (duplicate == null) {
				items2.add(item);
			}
		}
		return items2;
	}

	protected F createNonDuplicateItem(F item) {
		List<F> items = getCollidingItems(item);
		for (F i : items) {
			if (isDuplicate(item, i)) {
				return i;
			}
		}
		ItemService is = new ItemService();
		is.addItem(item);
		return null;
	}

	protected abstract List<F> getCollidingItems(F item);

	protected UserItem createUserItem(UserSource us, F item) {
		UserItem ui = new UserItem();
		ui.setAdded(item.getAdded());
		ui.setItem(item.getKey());
		ui.setRead(false);
		ui.setUserId(us.getUserId());
		ui.setUserSource(us.getKey());
		ui.setLabels(us.getDefaultLabels());
		return ui;
	}

	protected void saveUserItems(List<UserItem> userItems) {
		ItemService is = new ItemService();
		is.saveUserItems(userItems);
		for (UserItem ui : userItems) {
			backupService.planForBackup(ui);
		}
	}

	protected List<UserSource> getUserSources() {
		SourceService ss = new SourceService();
		List<UserSource> activeUserSourcesForSource =
			ss.getActiveUserSourcesForSource(getSource().getKey());
		return activeUserSourcesForSource;
	}

	protected boolean isDuplicate(F i1, F i2) {
		if (i1.getSource().equals(i2.getSource())) {
			// same sources
			if (i1.getHash().equals(i2.getHash())) {
				// same hashes
				if ((i1.getUrl() != null && i2.getUrl() != null) ? i1
					.getUrl()
					.equals(i2.getUrl()) : i1.getUrl() == i2.getUrl()) {
					return true;
				}
			}
		}
		return false;
	}

	public E getSource() {
		return source;
	}
}
