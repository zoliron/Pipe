package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.*;



/**
 * The breadth first search algorithm iterative implementation.
 *
 * @author Ronen Zolicha
 */
public class BFS<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		Queue<SearcherNode<S>> queue = new LinkedList<>();
		Set<S> visited = new HashSet<>();

		// Add the initial state.
		SearcherNode<S> initialNode = getInitialNode(searchable);
		queue.add(initialNode);
		visited.add(initialNode.getState());

		while (!queue.isEmpty()){
			incrementIteration();

			SearcherNode<S> node = queue.remove();

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
				if (!visited.contains(possibleNode.getState())){
					visited.add(node.getState());
					queue.add(possibleNode);
				}
			}
		}

		return null;
	}



}
