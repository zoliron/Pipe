package com.zoliron.searchable;

import com.zoliron.searcher.SearcherNode;

import java.util.List;



/**
 * The searchable interface.
 *
 * @author Ronen Zolicha
 */
public interface Searchable<S>{



	/**
	 * Returns the initial searchable state.
	 */
	S getInitialState();



	/**
	 * Returns {@code true} if the specified node represents the goal state, otherwise returns {@code false}.
	 */
	boolean isGoal(SearcherNode<S> node);



	/**
	 * Returns all the possible states from the specified state.
	 */
	List<S> getAllPossibleStates(S state);



	/**
	 * Calculate the movement cost from {@param fromState} to {@param toState}.
	 */
	double calculateCost(S fromState, S toState);



	/**
	 * Estimate the cost to rich the goal state from the specified {@param state}.
	 */
	double calculateEstimation(S state);



}