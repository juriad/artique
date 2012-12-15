package cz.artique.server.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.CompositeCriterion;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.S3QueryResultList;

import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.source.Source;

public class ListOfItems extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<UserItem> userItems;

	private Boolean readCriterion;

	private Filter filter;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// User user = UserServiceFactory.getUserService().getCurrentUser();
		User user = new User("text@example.com", "gmail.com");
		test(user);
	}

	public void test(User user) {
		System.out.println("reading first 10 items:");

		SourceMeta meta = SourceMeta.get();

		CompositeFilterOperator operator = CompositeFilterOperator.OR;
		FilterCriterion[] criteria =
			new FilterCriterion[] {
				meta.version.equal(1L),
				meta.version.equal(6L) };
		CompositeCriterion criterion =
			new CompositeCriterion(meta, operator, criteria);

		S3QueryResultList<Source> uItems =
			Datastore
				.query(meta)
				.filter(criterion)
				//.filter(criteria[1])
				.sort(meta.version.desc)
				.sort(meta.key.asc)
				.limit(1)
				.asQueryResultList();

		for (Source ui : uItems) {
			System.out.println(ui);
		}

		System.out.println("storing cursor");

		String encodedCursor = uItems.getEncodedCursor();
		String encodedFilters = uItems.getEncodedFilter();
		String encodedSorts = uItems.getEncodedSorts();
		// You can determine if a next entry exists
		boolean hasNext = uItems.hasNext();
		// Store the encodedCursor...

		System.out.println("renewing");

		while (hasNext) {
			uItems =
				Datastore
					.query(meta)
					.encodedStartCursor(encodedCursor)
					.encodedFilter(encodedFilters)
					.encodedSorts(encodedSorts)
					.limit(1)
					.asQueryResultList();

			encodedCursor = uItems.getEncodedCursor();
			encodedFilters = uItems.getEncodedFilter();
			encodedSorts = uItems.getEncodedSorts();

			hasNext = uItems.hasNext();

			System.out.println("next 5 items:");
			for (Source ui : uItems) {
				System.out.println(ui);
			}

		}

	}

}
