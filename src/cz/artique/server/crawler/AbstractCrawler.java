package cz.artique.server.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.service.UserSourceService;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.Stats;
import cz.artique.shared.model.source.UserSource;

public abstract class AbstractCrawler<E extends Source, F extends Item>
		extends Fetcher implements Crawler<E> {

	private final E source;

	protected AbstractCrawler(E source) {
		this.source = source;
	}

	protected void writeStat(int items) {
		Stats s = new Stats();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(items);
		Datastore.put(s);
	}

	protected void writeStat(Throwable t) {
		Stats s = new Stats();
		s.setProbeDate(new Date());
		s.setSource(getSource().getKey());
		s.setItemsAcquired(0);
		s.setError(t.getLocalizedMessage());
		Datastore.put(s);
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
		Key key = Datastore.put(item);
		item.setKey(key);
		return null;
	}

	protected abstract List<F> getCollidingItems(F item);

	protected UserItem createUserItem(UserSource us, F item) {
		UserItem ui = new UserItem();
		ui.setAdded(item.getAdded());
		ui.setItem(item.getKey());
		ui.setRead(false);
		ui.setUser(us.getUser());
		ui.setUserSource(us.getKey());
		ui.setLabels(us.getDefaultLabels());
		return ui;
	}

	protected List<UserSource> getUserSources() {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore
				.query(meta)
				.filter(meta.source.equal(getSource().getKey()))
				.filter(meta.watching.equal(Boolean.TRUE))
				.asList();
		UserSourceService uss = new UserSourceService();
		uss.fillRegions(userSources);
		return userSources;
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
