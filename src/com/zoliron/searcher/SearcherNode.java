package com.zoliron.searcher;

import java.util.LinkedList;
import java.util.List;



/**
 * Encapsulate the {@link Searcher} crucial information through the algorithm run.
 *
 * @author Ronen Zolicha
 */
public class SearcherNode<S> implements Comparable<SearcherNode<S>>{



	/**
	 * The previous node.
	 */
	private final SearcherNode<S> previousNode;



	/**
	 * The node state.
	 */
	private final S state;



	/**
	 * The path size.
	 */
	private final int pathSize;



	/**
	 * The cost for arriving to this state.
	 */
	private final double cost;



	/**
	 * The estimation to rich the goal state from this state.
	 */
	private final double estimation;



	/**
	 * The node score.
	 */
	private final double score;



	/**
	 * Creates new {@link SearcherNode} with the specified parameters.
	 */
	SearcherNode(SearcherNode<S> previousNode, S state, double cost, double estimation){
		this.previousNode = previousNode;
		this.state = state;
		this.pathSize = (previousNode != null) ? previousNode.getPathSize() + 1 : 1;
		this.cost = cost;
		this.estimation = estimation;
		this.score = cost + estimation;
	}



	/**
	 * Returns the previous node.
	 */
	public SearcherNode<S> getPreviousNode(){
		return previousNode;
	}



	/**
	 * Returns the node state.
	 */
	public S getState(){
		return state;
	}



	/**
	 * Returns the path size.
	 */
	public int getPathSize(){
		return pathSize;
	}



	/**
	 * Returns the nodes path.
	 */
	public List<SearcherNode<S>> path(){
		LinkedList<SearcherNode<S>> path = new LinkedList<>();

		SearcherNode<S> current = this;
		while (current != null){
			path.addFirst(current); // Add the items in reverse mode.

			current = current.previousNode;
		}

		return path;
	}



	/**
	 * Returns the cost for arriving to this state.
	 */
	public double getCost(){
		return cost;
	}



	/**
	 * Returns the estimation to rich the goal state from this state.
	 */
	public double getEstimation(){
		return estimation;
	}



	/**
	 * Returns the node score.
	 */
	public double getScore(){
		return score;
	}



	@Override
	public int compareTo(SearcherNode<S> other){
		return Double.compare(score, other.score);
	}



}