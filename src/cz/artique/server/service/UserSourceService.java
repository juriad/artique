package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.ServerUtils;
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
			userSource.setKey(key);

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
			Source s = Datastore.get(tx, SourceMeta.get(), us.getSource());
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
	
}
