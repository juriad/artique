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

/**
 * Abstract base for all {@link Crawler}s; it handles {@link CheckStat}s,
 * creating {@link Item}s and {@link UserItem}s. This abstract class does not
 * contain any processing logic.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            {@link Source} type
 * @param <F>
 *            item type which is produced by crawled {@link Source}
 */
public abstract class AbstractCrawler<E extends Source, F extends Item>
		extends Fetcher implements Crawler<E> {

	private final E source;

	private final BackupService backupService;

	protected AbstractCrawler(E source) {
		this.source = source;
		backupService = new BackupService();
	}

	/**
	 * Saves a new {@link CheckStat} describing check when it was successful.
	 * 
	 * @param items
	 *            number of items acquired
	 */
	protected void writeStat(int items) {
		CheckStat s = new CheckStat();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(items);
		SourceService ss = new SourceService();
		ss.addStat(s);
	}

	/**
	 * Saves a new {@link CheckStat} describing this check when it failed.
	 * 
	 * @param t
	 *            exception which occurred while crawling
	 */
	protected void writeStat(Throwable t) {
		CheckStat s = new CheckStat();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(0);
		s.setError(t.getLocalizedMessage());
		SourceService ss = new SourceService();
		ss.addStat(s);
	}

	/**
	 * @param items
	 *            {@link Item}s to create if they already does not exist
	 * @return list of {@link Item}s which had not existed
	 */
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

	/**
	 * @param item
	 *            {@link Item} to create if it does no exist yet
	 * @return original item if already existed, null if item has been created
	 */
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

	/**
	 * @param item
	 *            {@link Item} which are the colliding item searched for
	 * @return list of colliding items
	 */
	protected abstract List<F> getCollidingItems(F item);

	/**
	 * @param us
	 *            {@link UserSource} the new {@link UserItem} belongs to
	 * @param item
	 *            {@link Item} the new {@link UserItem} is personalization of
	 * @return prototype of {@link UserItem}
	 */
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

	/**
	 * Saves list of {@link UserItem}s at once.
	 * 
	 * @param userItems
	 *            list of {@link UserItem}s to save
	 */
	protected void saveUserItems(List<UserItem> userItems) {
		if (userItems == null || userItems.size() == 0) {
			return;
		}
		ItemService is = new ItemService();
		is.saveUserItems(userItems);
		for (UserItem ui : userItems) {
			backupService.planForBackup(ui);
		}
	}

	/**
	 * @return list of active {@link UserSource}s for crawled {@link Source}.
	 */
	protected List<UserSource> getUserSources() {
		SourceService ss = new SourceService();
		List<UserSource> activeUserSourcesForSource =
			ss.getActiveUserSourcesForSource(getSource().getKey());
		return activeUserSourcesForSource;
	}

	/**
	 * {@link Item} is duplicate if it has the same {@link Source}, their hashes
	 * and URLs equal each other.
	 * 
	 * @param i1
	 *            the first item
	 * @param i2
	 *            the second item
	 * @return whether i1 is duplicate of i2
	 */
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

	/**
	 * @return source
	 */
	public E getSource() {
		return source;
	}
}
