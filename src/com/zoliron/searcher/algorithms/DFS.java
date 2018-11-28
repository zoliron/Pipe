package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * The depth first search algorithm recursive implementation.
 *
 * @author Ronen Zolicha
 */
public class DFS<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		SearcherNode<S> initial = getInitialNode(searchable);

		return dfs(searchable, initial, new HashSet<>());
	}



	/**
	 * Performs a DFS visit.
	 */
	private Solution<S> dfs(Searchable<S> searchable, SearcherNode<S> node, Set<S> visited){
		incrementIteration();

		// Mark as visited.
		visited.add(node.getState());

		// Check if we are on the goal state.
		if (searchable.isGoal(node))
			return toSolution(node);

		List<SearcherNode<S>> possibleNodes = getAllPossibleNodes(searchable, node);
		for (SearcherNode<S> possibleNode : possibleNodes){
			if (!visited.contains(possibleNode.getState())){
				Solution<S> solution = dfs(searchable, possibleNode, visited);
				if (solution != null)
					return solution;
			}
		}

		return null;
	}



}