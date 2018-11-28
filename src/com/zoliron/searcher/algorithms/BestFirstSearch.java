package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.*;



/**
 * The best first search algorithm implementation.
 */
public class BestFirstSearch<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		Queue<SearcherNode<S>> queue = new PriorityQueue<>();

		Map<S, SearcherNode<S>> open = new HashMap<>();
		Set<S> closed = new HashSet<>();

		// Add the initial state.
		SearcherNode<S> initialNode = getInitialNode(searchable);
		open.put(initialNode.getState(), initialNode);
		queue.add(initialNode);

		while (!queue.isEmpty()){
			incrementIteration();

			SearcherNode<S> node = queue.remove();
			if (open.remove(node.getState()) == null)
				throw new IllegalStateException("Open node does not in queue!");
			closed.add(node.getState());

//			System.out.println(node.getState());
//			System.out.println("Score: " + node.getScore());
//			System.out.println("Cost: " + node.getCost());
//			System.out.println("Estimation: " + node.getEstimation());
//			System.out.println();

			// Check if we are on the goal state.
			if (searchable.isGoal(node))
				return toSolution(node);

			List<SearcherNode<S>> possibleNodes = getAllPossibleNodes(searchable, node);
			for (SearcherNode<S> possibleNode : possibleNodes){
				S possibleState = possibleNode.getState();
				SearcherNode<S> openNode = open.get(possibleState);

				if (!closed.contains(possibleState) && (openNode == null)){
					open.put(possibleState, possibleNode);
					queue.add(possibleNode);
				} else if (isBetterThan(possibleNode, openNode)){
					if (!queue.remove(openNode))
						throw new IllegalStateException("Open node does not in queue!");

					open.put(possibleState, possibleNode);
					queue.add(possibleNode);
				}
			}
		}

		return null;
	}



	/**
	 * Returns {@code true} if {@param n1} is better than {@code n2} and have the same state, otherwise
	 * returns {@code true}.
	 */
	private boolean isBetterThan(SearcherNode<S> n1, SearcherNode<S> n2){
		return (n1 != null)
				&& (n2 != null)
				&& Objects.equals(n1.getState(), n2.getState())
				&& (n1.getCost() < n2.getCost());
	}



}