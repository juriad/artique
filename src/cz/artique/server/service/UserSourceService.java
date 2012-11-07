package cz.artique.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.meta.source.SourceMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.shared.model.hierarchy.Hierarchy;
import cz.artique.shared.model.hierarchy.HierarchyChangeType;
import cz.artique.shared.model.hierarchy.HierarchyLeaf;
import cz.artique.shared.model.hierarchy.HierarchyUtils;
import cz.artique.shared.model.hierarchy.HierarchyVertex;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.utils.SourceException;

public class UserSourceService {

	private final User user;

	public UserSourceService(User user) {
		this.user = user;
	}

	public void commitHierarchy(Hierarchy<UserSource> hierarchy) {
		Map<UserSource, HierarchyChangeType> changes =
			new HashMap<UserSource, HierarchyChangeType>();
		hierarchy.getChanges(changes);
		List<UserSource> unwatch = new ArrayList<UserSource>();
		List<UserSource> watch = new ArrayList<UserSource>();
		List<UserSource> changed = new ArrayList<UserSource>();

		for (java.util.Map.Entry<UserSource, HierarchyChangeType> e : changes
			.entrySet()) {
			switch (e.getValue()) {
			case ADDED:
				watch.add(e.getKey());
				break;
			case CHANGED:
				changed.add(e.getKey());
				break;
			case CHANGED_ORDER:
				throw new SourceException("This shall never happen");
			case NO_CHANGE:
				throw new SourceException("This shall never happen");
			case REMOVED:
				unwatch.add(e.getKey());
				break;
			}
		}

		for (UserSource u : unwatch) {
			unwatchSource(u);
		}

		for (UserSource u : watch) {
			watchSource(u);
		}

		Datastore.put(changed);
	}

	public ManualSource createManualSourceAndWatch(
			Hierarchy<UserSource> hierarchy) {
		ManualSource s = new ManualSource(user);
		s.setUsage(1);
		Key key = Datastore.allocateId(ManualSourceMeta.get());
		s.setKey(key);

		watchSource(hierarchy, s, "manual source for " + user.getNickname());

		Datastore.put(s);
		return s;
	}

	public Hierarchy<UserSource> getHierarchy(String prefix) {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> userSources =
			Datastore
				.query(meta)
				.filter(meta.user.equal(user))
				.filter(meta.hierarchy.startsWith(prefix))
				.sort(meta.hierarchy.asc)
				.asList();

		HierarchyVertex<UserSource> root =
			new HierarchyVertex<UserSource>(null, "root", 0);

		Map<String, Hierarchy<UserSource>> map =
			new HashMap<String, Hierarchy<UserSource>>();
		map.put(HierarchyUtils.root, root);

		for (UserSource u : userSources) {
			String h = u.getHierarchy();
			List<String> prefixes = HierarchyUtils.getPrefixes(h);
			Hierarchy<UserSource> parent;
			for (int i = 1; i < prefixes.size() - 1; i++) {
				if (map.containsKey(prefixes.get(i - 1))) {
					continue;
				}

				parent = map.get(prefixes.get(i - 1));
				String p = prefixes.get(i);
				String[] orderAndName = HierarchyUtils.getOrderAndName(p);
				HierarchyVertex<UserSource> v =
					new HierarchyVertex<UserSource>(parent, orderAndName[1],
						Integer.valueOf(orderAndName[0]));
				parent.getChildren().add(v);
				map.put(p, v);
			}
			parent = map.get(prefixes.get(prefixes.size() - 1));
			HierarchyLeaf<UserSource> leaf =
				new HierarchyLeaf<UserSource>(u, parent, parent
					.getChildren()
					.size());
			parent.getChildren().add(leaf);
		}

		return root;
	}

	public void unwatchSource(Hierarchy<UserSource> hierarchy,
			UserSource userSource) {
		if (hierarchy instanceof HierarchyLeaf<?>) {
			if (((HierarchyLeaf<UserSource>) hierarchy).getE().equals(
				userSource)) {
				hierarchy.getParent().removeChild(hierarchy);
			}
			return;
		}

		for (Hierarchy<UserSource> child : hierarchy.getChildren()) {
			unwatchSource(child, userSource);
		}
	}

	public void unwatchSource(UserSource userSource) {
		Key sk = userSource.getSource();
		Transaction tx = Datastore.beginTransaction();
		Source s = Datastore.get(tx, SourceMeta.get(), sk);
		s.setUsage(s.getUsage() - 1);

		if (s instanceof HTMLSource) {

		}

		Datastore.put(tx, s);
		tx.commit();
		Datastore.delete(userSource.getKey());
	}

	public UserSource watchSource(Hierarchy<UserSource> hierarchy,
			Source source, String name) {
		UserSource us = new UserSource(user, source, name);

		hierarchy.addChild(new HierarchyLeaf<UserSource>(us));

		Key key = Datastore.allocateId(UserSourceMeta.get());
		us.setKey(key);
		return us;
	}

	public void watchSource(UserSource userSource) {
		Key sk = userSource.getSource();
		Transaction tx = Datastore.beginTransaction();
		Source s = Datastore.get(tx, SourceMeta.get(), sk);
		s.setUsage(s.getUsage() + 1);
		Datastore.put(tx, s);
		tx.commit();
		Datastore.put(userSource);
	}
}
