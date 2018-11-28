package com.zoliron.searcher;

import com.zoliron.searchable.Searchable;



/**
 * The searcher interface.
 *
 * @author Ronen Zolicha
 */
public interface Searcher<S>{



	/**
	 * Run the search algorithm on the specified searchable and returns the solution; Returns {@code null} if
	 * does not succeeded to find any solution.
	 */
	Solution<S> search(Searchable<S> searchable);



}
