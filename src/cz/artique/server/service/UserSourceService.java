package cz.artique.server.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.server.meta.label.LabelMeta;
import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

public class UserSourceService {

	public UserSourceService() {}

	public UserSource creatIfNotExist(UserSource userSource) {
		Transaction tx = Datastore.beginTransaction();
		Key key = ServerUtils.genKey(userSource);
		UserSource theUserSource =
			Datastore.getOrNull(tx, UserSourceMeta.get(), key);
		if (theUserSource == null) {
			List<Key> defaultLabels = new ArrayList<Key>();
			{
				Key labelKey = Datastore.allocateId(LabelMeta.get());
				String labelName =
					userSource.getName() + "$"
						+ KeyFactory.keyToString(labelKey);
				final Label l = new Label(userSource.getUser(), labelName);
				l.setLabelType(LabelType.USER_SOURCE);
				l.setKey(ServerUtils.genKey(l));
				Datastore.put(l);

				userSource.setKey(key);
				defaultLabels.add(l.getKey());
			}
			userSource.setDefaultLabels(defaultLabels);
			updateUsage(userSource, tx);
			Datastore.put(tx, userSource);
			theUserSource = userSource;
		}
		tx.commit();
		return theUserSource;
	}

	public void updateUserSource(UserSource userSource) {
		Transaction tx = Datastore.beginTransaction();
		updateUsage(userSource, tx);
		Datastore.put(tx, userSource);
		tx.commit();
	}

	private void updateUsage(UserSource us, Transaction tx) {
		UserSource original =
			Datastore.getOrNull(tx, UserSourceMeta.get(), us.getKey());
		int diff = 0;
		if (original == null) {
			// new usersource
			if (us.isWatching()) {
				diff = 1;
			} else {
				// new but not watching, do nothing
			}
		} else if (us.isWatching() && !original.isWatching()) {
			diff = 1;
		} else if (!us.isWatching() && original.isWatching()) {
			diff = -1;
		}

		if (diff != 0) {
			Source s;
			if (us.getSourceObject() == null) {
				s = Datastore.get(tx, SourceMeta.get(), us.getSource());
			} else {
				s = us.getSourceObject();
			}
			s.setUsage(s.getUsage() + diff);
			s.setEnabled(s.getUsage() > 0);
			Datastore.put(tx, s);

			if (s.getParent() != null) {
				Source s2 = Datastore.get(tx, SourceMeta.get(), s.getParent());
				s2.setUsage(s2.getUsage() + diff);
				s2.setEnabled(s2.getUsage() > 0);
				Datastore.put(tx, s2);
			}
		}
	}

	public List<UserSource> getUserSources(User user) {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();
		return userSources;
	}

	public UserSource getManualSource() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ManualSource ms = new ManualSource(user);
		ManualSource manualSource =
			Datastore.get(ManualSourceMeta.get(), ServerUtils.genKey(ms));
		UserSource us = new UserSource(user, manualSource, "");
		UserSource userSource =
			Datastore.get(UserSourceMeta.get(), ServerUtils.genKey(us));
		userSource.setSourceObject(manualSource);
		return userSource;
	}

	public UserSource ensureManualSource() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		ManualSource ms = new ManualSource(user);
		ManualSource manualSource =
			Datastore.getOrNull(ManualSourceMeta.get(), ServerUtils.genKey(ms));
		if (manualSource == null) {
			ms.setKey(ServerUtils.genKey(ms));
			ms.setEnabled(false);
			ms.setUsage(0);
			Datastore.put(ms);
			manualSource = ms;
		}
		UserSource us = new UserSource(user, manualSource, "");
		UserSource userSource =
			Datastore.getOrNull(UserSourceMeta.get(), ServerUtils.genKey(us));
		if (userSource == null) {
			us.setKey(ServerUtils.genKey(us));
			us.setWatching(true);
			Datastore.put(us);
			userSource = us;
		}
		userSource.setSourceObject(manualSource);
		return userSource;
	}

}
