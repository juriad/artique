package cz.artique.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.utils.TransactionException;

public class UserSourceService {

	public UserSourceService() {}

	public UserSource creatIfNotExist(UserSource userSource) {
		UserSource old = createUserSourceIfNotExist(userSource);
		if (old != null) {
			userSource = old;

			Label l = createLabelForUserSource(userSource);
			userSource.setLabel(l.getKey());
			List<Key> defaultLabels = userSource.getDefaultLabels();
			defaultLabels.add(l.getKey());
		}

		updateUsage(null, userSource);
		return userSource;
	}

	private UserSource createUserSourceIfNotExist(UserSource userSource) {
		Transaction tx = Datastore.beginTransaction();
		UserSource theUserSource = null;
		try {
			Key key = ServerUtils.genKey(userSource);
			theUserSource = Datastore.getOrNull(tx, UserSourceMeta.get(), key);
			if (theUserSource == null) {
				userSource.setKey(key);
				Datastore.put(tx, userSource);
			}
			tx.commit();
		} catch (Exception e) {
			throw new TransactionException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return theUserSource;
	}

	private Label createLabelForUserSource(UserSource userSource) {
		Key labelKey;
		String labelName = KeyFactory.keyToString(userSource.getSource());
		final Label l = new Label(userSource.getUser(), labelName);
		l.setLabelType(LabelType.USER_SOURCE);
		labelKey = ServerUtils.genKey(l);
		l.setKey(labelKey);
		Datastore.put(l);
		return l;
	}

	public void updateUserSource(UserSource userSource) {
		Transaction tx = Datastore.beginTransaction();
		UserSource original;
		try {
			original =
				Datastore.get(tx, UserSourceMeta.get(), userSource.getKey());
			Datastore.put(tx, userSource);
			tx.commit();
		} catch (Exception e) {
			throw new TransactionException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		updateUsage(original, userSource);
	}

	private void updateUsage(UserSource original, UserSource userSource) {
		int diff = 0;
		if (original == null) {
			// new usersource
			if (userSource.isWatching()) {
				diff = 1;
			} else {
				// new but not watching, do nothing
			}
		} else {
			// updating usersource
			if (userSource.isWatching() && !original.isWatching()) {
				// start watching
				diff = 1;
			} else if (!userSource.isWatching() && original.isWatching()) {
				// stop watching
				diff = -1;
			}
		}

		if (diff == 0) {
			return;
		}

		Transaction tx = Datastore.beginTransaction();
		try {
			Source source =
				Datastore.get(tx, SourceMeta.get(), userSource.getSource());
			// extra for Manual Source, disables crawling
			if (source instanceof ManualSource) {
				source.setUsage(0);
				source.setEnabled(false);
			} else {
				source.setUsage(source.getUsage() + diff);
				source.setEnabled(source.getUsage() > 0);
			}
			Datastore.put(tx, source);
		} catch (Exception e) {
			throw new TransactionException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public List<UserSource> getUserSources(User user) {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();

		Map<Key, UserSource> map = new HashMap<Key, UserSource>();
		for (UserSource userSource : userSources) {
			map.put(userSource.getSource(), userSource);
		}
		List<Source> sources = Datastore.get(SourceMeta.get(), map.keySet());
		for (Source source : sources) {
			map.get(source.getKey()).setSourceObject(source);
		}

		return userSources;
	}

	public UserSource getManualUserSource() {
		SourceService ss = new SourceService();
		ManualSource manualSource = ss.getManualSource();

		User user = UserServiceFactory.getUserService().getCurrentUser();
		UserSource us = new UserSource(user, manualSource, "does_not_matter");
		UserSource userSource =
			Datastore.get(UserSourceMeta.get(), ServerUtils.genKey(us));
		userSource.setSourceObject(manualSource);
		return userSource;
	}

	public UserSource ensureManualSource() {
		SourceService ss = new SourceService();
		ManualSource manualSource = ss.ensureManualSource();

		User user = UserServiceFactory.getUserService().getCurrentUser();
		UserSource us = new UserSource(user, manualSource, "");
		String name =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.MANUAL_SOURCE_NAME)
				.<String> get();
		us.setName(name);
		us.setKey(ServerUtils.genKey(us));
		us.setWatching(true);
		UserSource userSource = creatIfNotExist(us);
		userSource.setSourceObject(manualSource);
		return userSource;
	}

}
