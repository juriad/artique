package cz.artique.server.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.S3QueryResultList;

import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.item.ItemMeta;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Filter;

public class ListOfItems extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<UserItem> userItems;

	private Boolean readCriterion;

	private Filter filter;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//User user = UserServiceFactory.getUserService().getCurrentUser();
		User user = new User("text@example.com", "gmail.com");
		test(user);
	}

	public void test(User user) {
		System.out.println("reading first 10 items:");
		
		ItemMeta meta = ItemMeta.get();
		
		CompositeFilterOperator operator = CompositeFilterOperator.OR;
        FilterCriterion[] criteria =
            new FilterCriterion[] {
                meta.hash.startsWith("5"),
                meta.hash.startsWith("8") };
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);


		S3QueryResultList<Item> uItems =
			Datastore
				.query(meta)
				.filter(meta.hash.startsWith("5"))
				.sort(meta.added.desc)
				.limit(10)
				.asQueryResultList();

		for (Item ui : uItems) {
			System.out.println(ui);
		}

		System.out.println("storing cursor");

		String encodedCursor = uItems.getEncodedCursor();
		String encodedFilters = uItems.getEncodedFilters();
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
					.encodedFilters(encodedFilters)
					.encodedSorts(encodedSorts)
					.limit(5)
					.asQueryResultList();

			encodedCursor = uItems.getEncodedCursor();
			encodedFilters = uItems.getEncodedFilters();
			encodedSorts = uItems.getEncodedSorts();

			hasNext = uItems.hasNext();

			System.out.println("next 5 items:");
			for (Item ui : uItems) {
				System.out.println(ui);
			}

		}

	}

}
