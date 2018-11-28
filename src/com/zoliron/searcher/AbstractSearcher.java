package com.zoliron.searcher;

import com.zoliron.searchable.Searchable;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;



/**
 * An abstract {@link Searcher}.
 *
 * @author Ronen Zolicha
 */
public abstract class AbstractSearcher<S> implements Searcher<S>{



	/**
	 * The total number od iterations.
	 */
	private int numOfIterations = 0;



	/**
	 * Increment the iteration counter.
	 */
	protected void incrementIteration(){
		numOfIterations += 1;

//		System.out.print("\rIterations: " + numOfIterations);
	}



	/**
	 * Returns the total number od iterations.
	 */
	public int getNumOfIterations(){
		return numOfIterations;
	}



	/**
	 * Returns the initial node.
	 */
	protected SearcherNode<S> getInitialNode(Searchable<S> searchable){
		return toNode(searchable, null, searchable.getInitialState());
	}



	/**
	 * Returns all the possible nodes from the specified node.
	 */
	protected List<SearcherNode<S>> getAllPossibleNodes(Searchable<S> searchable, SearcherNode<S> node){
		List<S> possibleStates = searchable.getAllPossibleStates(node.getState());

		return possibleStates.stream()
				.map(state -> toNode(searchable, node, state))
				.collect(Collectors.toList());
	}



	/**
	 * Creates new {@link SearcherNode} with the specified searchable, previous node and state.
	 */
	private SearcherNode<S> toNode(Searchable<S> searchable, SearcherNode<S> previousNode, S toState){
		S previousState = (previousNode != null) ? previousNode.getState() : null;
		double cost = (previousState != null)
				? searchable.calculateCost(previousState, toState)
				: 0d;
		double accumulatedCost = (previousNode != null) ? previousNode.getCost() + cost : cost;
		double estimation = searchable.calculateEstimation(toState);

		return new SearcherNode<>(previousNode, toState, accumulatedCost, estimation);
	}



	/**
	 * Creates new {@link Solution} from the specified goal node.
	 */
	protected Solution<S> toSolution(SearcherNode<S> goal){
		List<S> states = goal.path()
				.stream()
				.map(SearcherNode::getState)
				.collect(Collectors.toList());

		return new Solution<>(states, goal.getCost(), getNumOfIterations(), Duration.ZERO);
	}



}