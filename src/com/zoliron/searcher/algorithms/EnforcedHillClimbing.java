package com.zoliron.searcher.algorithms;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.AbstractSearcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;



/**
 * The enforced hill climbing algorithm.
 *
 * @author Ronen Zolicha
 */
public class EnforcedHillClimbing<S> extends AbstractSearcher<S>{



	@Override
	public Solution<S> search(Searchable<S> searchable){
		Queue<SearcherNode<S>> queue = new LinkedList<>();

		// Add the initial state.
		SearcherNode<S> initialNode = getInitialNode(searchable);
		queue.add(initialNode);
		double bestScore = initialNode.getEstimation();

		while (!queue.isEmpty()){
			incrementIteration();

			SearcherNode<S> node = queue.remove();

			// Check if we are on the goal state.
			if (searchable.isGoal(node))
				return toSolution(node);

			List<SearcherNode<S>> possibleNodes = getAllPossibleNodes(searchable, node);
			for (SearcherNode<S> possibleNode : possibleNodes){
				double score = possibleNode.getScore();

				// Is this possible node better? (has lower score)
				// Hill climbing, just select the best node.
				if (score < bestScore){
					bestScore = score;

					queue.clear();
					queue.add(possibleNode);
					break;
				}

				// Add the possible node to the queue to perform BFS search (enforced Hill Climbing).
				queue.add(possibleNode);
			}
		}

		return null;
	}



}
