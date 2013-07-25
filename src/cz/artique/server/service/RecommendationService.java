package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.structure.Field;
import org.jscience.mathematics.vector.SparseMatrix;
import org.jscience.mathematics.vector.SparseVector;
import org.jscience.mathematics.vector.Vector;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.meta.recomandation.RecommendationMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;

/**
 * Calculates and serves {@link Recommendation} of {@link Source}s the user may
 * be interested in.
 * 
 * @author Adam Juraszek
 * 
 */
public class RecommendationService {

	/**
	 * Vector filled with one single value.
	 * 
	 * @author Adam Juraszek
	 * 
	 * @param <F>
	 *            type of vector
	 */
	class AllSame<F extends Field<F>> extends Vector<F> {

		private final F val;
		private final int dimenstion;

		public AllSame(int dimenstion, F val) {
			this.dimenstion = dimenstion;
			this.val = val;
		}

		@Override
		public int getDimension() {
			return dimenstion;
		}

		@Override
		public F get(int i) {
			return val;
		}

		@Override
		public Vector<F> opposite() {
			return null;
		}

		@Override
		public Vector<F> plus(Vector<F> that) {
			return null;
		}

		@Override
		public Vector<F> times(F k) {
			return null;
		}

		@Override
		public F times(Vector<F> that) {
			return null;
		}

		@Override
		public Vector<F> copy() {
			return null;
		}
	}

	/**
	 * Wraps consumer-product matrix, list of users and list of sources.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	static class Mapping {
		List<String> users;
		List<Key> sources;
		SparseMatrix<Float64> matrix;

		public Mapping(List<String> users, List<Key> sources,
				SparseMatrix<Float64> matrix) {
			super();
			this.users = users;
			this.sources = sources;
			this.matrix = matrix;
		}
	}

	/**
	 * Result score of calculation for one {@link Source} identified by its
	 * index.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	static class Result {
		int sourceIndex;
		double score;

		public Result(int sourceIndex, double score) {
			super();
			this.sourceIndex = sourceIndex;
			this.score = score;
		}
	}

	public RecommendationService() {}

	/**
	 * Main method which starts calculation.
	 * 
	 * The calculation consists of these steps:
	 * <ol>
	 * <li>gets matrix from database,
	 * <li>normalizes matrix separately both horizontally and vertically,
	 * <li>does the iterative matrix multiplication
	 * <li>saves recommendation
	 * </ol>
	 */
	public void recalc() {
		Mapping mapping = getConsumerProductMatrix();
		if (mapping == null) {
			return; // there is nobody to calculate recommendation for
		}
		SparseMatrix<Float64> Ct = makeC(mapping.matrix).transpose();
		SparseMatrix<Float64> B = makeB(mapping.matrix);
		SparseMatrix<Float64> CR0 = makeCR0(mapping.matrix.getNumberOfRows());

		SparseMatrix<Float64> PR = null;
		SparseMatrix<Float64> CR = CR0;

		int iterations =
			ConfigService.CONFIG_SERVICE.getConfig(
				ServerConfigKey.RECOMMENDATION_ITERATIONS).get();
		for (int i = 0; i < iterations; i++) {
			PR = CR.times(B);
			CR = PR.times(Ct).plus(CR0);
		}

		saveRecommendations(mapping, PR);
	}

	/**
	 * Saves recommendation.
	 * 
	 * @param mapping
	 *            contains original state and list of users and sources
	 * @param PR
	 *            result consumer-product matrix
	 */
	private void saveRecommendations(Mapping mapping, SparseMatrix<Float64> PR) {
		SparseMatrix<Float64> matrix = mapping.matrix;
		List<Recommendation> recommendations = new ArrayList<Recommendation>();

		for (int i = 0; i < PR.getNumberOfRows(); i++) {
			SparseVector<Float64> row = PR.getRow(i);
			List<Result> results = new ArrayList<Result>();
			for (int j = 0; j < row.getDimension(); j++) {
				// ZERO = new source, ONE = already watching
				if (matrix.get(i, j).doubleValue() < 0.5
					&& PR.get(i, j).doubleValue() > 0) {
					results.add(new Result(j, PR.get(i, j).doubleValue()));
				}
			}
			Recommendation recommendation =
				createRecommendation(mapping.users.get(i), mapping.sources,
					results);
			recommendations.add(recommendation);
		}

		if (!recommendations.isEmpty()) {
			Datastore.put(recommendations);
		}
	}

	/**
	 * Creates recommendation of {@link Source}s for user.
	 * 
	 * @param user
	 *            the user the {@link Recommendation} is created for
	 * @param sources
	 *            list of all {@link Source}s
	 * @param results
	 *            list of result {@link Source}s with scores
	 * @return created {@link Recommendation}
	 */
	private Recommendation createRecommendation(String user, List<Key> sources,
			List<Result> results) {
		List<Key> bestRecommendations =
			getBestRecommendations(results, sources);
		Recommendation rec = new Recommendation(user);
		rec.setRecommendedSources(bestRecommendations);
		Key key = KeyGen.genKey(rec);
		rec.setKey(key);
		return rec;
	}

	/**
	 * Gets the best few {@link Source}s according to their score from list of
	 * all {@link Source}s.
	 * 
	 * @param results
	 *            list of scored {@link Source}s
	 * @param sourceKeys
	 *            list of all {@link Source}s
	 * @return list of best {@link Source}s
	 */
	private List<Key> getBestRecommendations(List<Result> results,
			List<Key> sourceKeys) {
		int recommendations =
			ConfigService.CONFIG_SERVICE.getConfig(
				ServerConfigKey.RECOMMENDATION_COUNT).get();

		Collections.sort(results, new Comparator<Result>() {
			public int compare(Result o1, Result o2) {
				return ((Double) o1.score).compareTo(o2.score);
			}
		});

		List<Key> sources = new ArrayList<Key>();
		for (int i = 0; i < recommendations && i < results.size(); i++) {
			Result res = results.get(i);
			sources.add(sourceKeys.get(res.sourceIndex));
		}
		return sources;
	}

	/**
	 * Makes matrix with ones on diagonal.
	 * 
	 * @param numberOfRows
	 *            number of rows
	 * @return matrix
	 */
	private SparseMatrix<Float64> makeCR0(int numberOfRows) {
		List<SparseVector<Float64>> matrix =
			new ArrayList<SparseVector<Float64>>();
		for (int i = 0; i < numberOfRows; i++) {
			matrix.add(SparseVector.valueOf(numberOfRows, Float64.ZERO, i,
				Float64.ONE));
		}
		return SparseMatrix.valueOf(new AllSame<Float64>(numberOfRows,
			Float64.ONE), Float64.ZERO);
	}

	/**
	 * Normalizes customers for each product.
	 * 
	 * @param matrix
	 *            consumer-product matrix
	 * @return normalized matrix
	 */
	private SparseMatrix<Float64> makeB(SparseMatrix<Float64> matrix) {
		// normalize customers for each product
		return makeC(matrix.transpose()).transpose();
	}

	/**
	 * Normalizes products for each customer.
	 * 
	 * @param matrix
	 *            consumer-product matrix
	 * @return normalized matrix
	 */
	private SparseMatrix<Float64> makeC(SparseMatrix<Float64> matrix) {
		// normalize products for each customer
		List<SparseVector<Float64>> normRows =
			new ArrayList<SparseVector<Float64>>();
		for (int i = 0; i < matrix.getNumberOfRows(); i++) {
			SparseVector<Float64> row = matrix.getRow(i);
			Float64 sum =
				row
					.times(new AllSame<Float64>(row.getDimension(), Float64.ONE));
			SparseVector<Float64> norm = row.times(sum.inverse());
			normRows.add(norm);
		}
		return SparseMatrix.valueOf(normRows);
	}

	/**
	 * Fetches and restores consumer-product matrix from database.
	 * 
	 * @return null if no {@link UserSource} exists
	 */
	private Mapping getConsumerProductMatrix() {
		UserSourceService uss = new UserSourceService();
		List<UserSource> allUserSources = uss.getAllUserSources();
		if (allUserSources.isEmpty()) {
			return null;
		}

		Map<String, List<Key>> users = new HashMap<String, List<Key>>();
		Set<Key> sources = new HashSet<Key>();
		for (UserSource us : allUserSources) {
			List<Key> sourcesForUser = users.get(us.getUserId());
			if (sourcesForUser == null) {
				sourcesForUser = new ArrayList<Key>();
				users.put(us.getUserId(), sourcesForUser);
			}
			Key sourceKey = us.getSource();
			sources.add(sourceKey);
			sourcesForUser.add(sourceKey);
		}
		List<Key> sourcesList = new ArrayList<Key>(sources);
		Map<Key, Integer> sourceIndices = new HashMap<Key, Integer>();
		int i = 0;
		for (Key key : sourcesList) {
			sourceIndices.put(key, i);
			i++;
		}

		List<String> usersList = new ArrayList<String>(users.keySet());
		List<SparseVector<Float64>> matrix =
			new ArrayList<SparseVector<Float64>>();
		for (String user : usersList) {
			SparseVector<Float64> userVector = null;
			List<Key> sourcesForUser = users.get(user);
			for (Key key : sourcesForUser) {
				int index = sourceIndices.get(key);
				SparseVector<Float64> v =
					SparseVector.valueOf(sources.size(), Float64.ZERO, index,
						Float64.ONE);
				if (userVector == null) {
					userVector = v;
				} else {
					userVector = userVector.plus(v);
				}
			}
			matrix.add(userVector);
		}

		return new Mapping(usersList, sourcesList, SparseMatrix.valueOf(matrix));
	}

	/**
	 * Gets {@link Recommendation} calculated for specified user
	 * 
	 * @param userId
	 *            user the {@link Recommendation} is desired for
	 * @return {@link Recommendation}
	 */
	public Recommendation getRecommendation(String userId) {
		Recommendation rec = new Recommendation(userId);
		Key key = KeyGen.genKey(rec);

		Recommendation recommadation =
			Datastore.getOrNull(RecommendationMeta.get(), key);
		if (recommadation != null
			&& recommadation.getRecommendedSources() != null
			&& !recommadation.getRecommendedSources().isEmpty()) {
			SourceService ss = new SourceService();
			List<Source> sourcesByKeys =
				ss.getSourcesByKeys(recommadation.getRecommendedSources());
			recommadation.setRecommendedSourcesObjects(sourcesByKeys);
		}
		return recommadation;
	}
}
