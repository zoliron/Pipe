package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.*;



/**
 * The A* algorithm implementation.
 *
 * @author Ronen Zolicha
 */
public class AStar<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		Queue<SearcherNode<S>> queue = new PriorityQueue<>();
		Map<S, SearcherNode<S>> open = new HashMap<>();
		Map<S, SearcherNode<S>> closed = new HashMap<>();

		// Add the initial state.
		SearcherNode<S> initialNode = getInitialNode(searchable);
		open.put(initialNode.getState(), initialNode);
		queue.add(initialNode);

		while (!open.values().isEmpty()){
			incrementIteration();

			SearcherNode<S> node = takePromising(queue, open);
			if (open.remove(node.getState()) == null)
				throw new IllegalStateException("Open node does not in queue!");

			// Check if we are on the goal state.
			if (searchable.isGoal(node))
				return toSolution(node);

			List<SearcherNode<S>> possibleNodes = getAllPossibleNodes(searchable, node);
			for (SearcherNode<S> possibleNode : possibleNodes){
				SearcherNode<S> possibleNodeOpen = open.get(possibleNode.getState());
				if (possibleNodeOpen != null && possibleNodeOpen.compareTo(possibleNode) <= 0)
					continue; // Keep analysing the other movements, discard this movement.

				SearcherNode<S> possibleNodeClosed = closed.get(possibleNode.getState());
				if (possibleNodeClosed != null && possibleNodeClosed.compareTo(possibleNode) <= 0)
					continue; // Check if this path improves the cost of a close neighbor.

				open.put(possibleNode.getState(), possibleNode);
				queue.add(possibleNode);
			}

			closed.put(node.getState(), node);
		}

		return null;
	}



	/**
	 * Poll until a valid state is found.
	 */
	private SearcherNode<S> takePromising(Queue<SearcherNode<S>> queue, Map<S, SearcherNode<S>> open){
		SearcherNode<S> node = queue.poll();

		while (!open.containsKey(node.getState()))
			node = queue.poll();

		return node;
	}



}
