package com.zoliron.games.pipe;

import com.zoliron.games.pipe.model.PipeBoard;
import com.zoliron.games.pipe.searchable.PipeSearchable;
import com.zoliron.games.pipe.searchable.PipeSearchableState;
import com.zoliron.searcher.Solution;
import com.zoliron.searcher.algorithms.BestFirstSearch;
import com.zoliron.solver.Solver;
import com.zoliron.utils.Point;



/**
 * The {@link PipeBoard} game solver.
 *
 * @author Ronen Zolicha
 */
public class PipeSolver implements Solver<PipeBoard>{



	@Override
	public PipeBoard createProblem(String problem){
		return new PipeBoardParser().parse(problem);
	}



	@Override
	public String solve(PipeBoard problem){
		PipeSearchable searchable = new PipeSearchable(problem);
		Solution<PipeSearchableState> solution = new BestFirstSearch<PipeSearchableState>().search(searchable);
		if (solution == null)
			return NO_SOLUTION;

		int[][] clickCount = countClicks(problem, solution);

		return toString(problem, clickCount);
	}



	/**
	 * Count the number of clicks on each cell of the specified {@link PipeBoard} according to the given solution.
	 */
	private int[][] countClicks(PipeBoard problem, Solution<PipeSearchableState> solution){
		int rowCount = problem.getRowCount();
		int columnCount = problem.getColumnCount();
		int[][] result = new int[rowCount][columnCount];

		for (PipeSearchableState state : solution.getStates()){
			Point action = state.getAction();
			if (action == null) // Happen only on the initial state.
				continue;

			int column = action.x;
			int row = action.y;

			result[row][column]++;
		}

		return result;
	}



	/**
	 * Returns the click solution string of the specified clicks count matrix.
	 */
	private String toString(PipeBoard problem, int[][] clickCount){
		int rowCount = problem.getRowCount();
		int columnCount = problem.getColumnCount();

		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < rowCount; row++){
			for (int column = 0; column < columnCount; column++){
				int clicksCount = clickCount[row][column];
				if (clicksCount <= 0)
					continue;

				if (sb.length() > 0) // Add line
					sb.append('\n');

				sb.append(row).append(',').append(column).append(',').append(clicksCount);
			}
		}

		return sb.toString();
	}



}