package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;



/**
 * The hill climbing algorithm.
 *
 * @author Ronen Zolicha
 */
public class HillClimbing<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		Queue<SearcherNode<S>> queue = new LinkedList<>();

		// Add the initial state.
		SearcherNode<S> initialNode = getInitialNode(searchable);
		queue.add(initialNode);

		while (!queue.isEmpty()){
			incrementIteration();

			SearcherNode<S> node = queue.remove();

			// Check if we are on the goal state.
			if (searchable.isGoal(node))
				return toSolution(node);

			// Find the best node.
			List<SearcherNode<S>> possibleNodes = getAllPossibleNodes(searchable, node);
			SearcherNode<S> bestNode = null;
			for (SearcherNode<S> possibleNode : possibleNodes){
				if (bestNode == null)
					bestNode = possibleNode;
				else if (possibleNode.compareTo(bestNode) < 0)
					bestNode = possibleNode;
			}

			// Add the best node to the queue.
			queue.add(bestNode);
		}

		return null;
	}



}
