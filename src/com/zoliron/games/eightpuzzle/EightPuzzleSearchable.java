package com.zoliron.games.eightpuzzle;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.utils.MathUtils;
import com.zoliron.utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/**
 * @author Ronen Zolicha
 */
public class EightPuzzleSearchable implements Searchable<EightPuzzleState>{



	/**
	 * The initial board.
	 */
	private final EightPuzzle puzzle;



	/**
	 * Creates new {@link EightPuzzleState} with the specified puzzle.
	 */
	public EightPuzzleSearchable(EightPuzzle puzzle){
		this.puzzle = puzzle;
	}



	@Override
	public EightPuzzleState getInitialState(){
		return new EightPuzzleState(puzzle, null, null);
	}



	@Override
	public boolean isGoal(SearcherNode<EightPuzzleState> node){
		return Objects.equals(node.getState().getPuzzle(), EightPuzzle.GOAL);
	}



	@Override
	public List<EightPuzzleState> getAllPossibleStates(EightPuzzleState state){
		EightPuzzle puzzle = state.getPuzzle();
		Point emptyPoint = puzzle.getEmptyPoint();

		List<EightPuzzleState> possibleStates = new ArrayList<>(4);
		EightPuzzleState rightState = tryAction(puzzle, emptyPoint, EightPuzzle.PuzzleMove.LEFT);
		if (rightState != null)
			possibleStates.add(rightState);

		EightPuzzleState bottomState = tryAction(puzzle, emptyPoint, EightPuzzle.PuzzleMove.UP);
		if (bottomState != null)
			possibleStates.add(bottomState);

		EightPuzzleState leftState = tryAction(puzzle, emptyPoint, EightPuzzle.PuzzleMove.RIGHT);
		if (leftState != null)
			possibleStates.add(leftState);

		EightPuzzleState topState = tryAction(puzzle, emptyPoint, EightPuzzle.PuzzleMove.DOWN);
		if (topState != null)
			possibleStates.add(topState);

		return possibleStates;
	}



	@Override
	public double calculateCost(EightPuzzleState fromState, EightPuzzleState toState){
		return (fromState == null) ? 0d : 1d;
	}



	@Override
	public double calculateEstimation(EightPuzzleState state){
		EightPuzzle puzzle = state.getPuzzle();

		double manhattanDistance = 0d;
		for (Point puzzlePoint : puzzle){
			String tile = puzzle.getTile(puzzlePoint);
			Point goalPoint = EightPuzzle.GOAL.getPoint(tile);

			manhattanDistance += MathUtils.manhattanDistance(puzzlePoint, goalPoint);
		}

		return manhattanDistance;
	}



	/**
	 * Try to perform the specified action; Returns a new {@link EightPuzzleState} if succeeded or {@code null} otherwise.
	 */
	private static EightPuzzleState tryAction(EightPuzzle puzzle, Point emptyPoint, EightPuzzle.PuzzleMove puzzleMove){
		Point neighbourPoint = puzzle.getPointForAction(emptyPoint, puzzleMove);
		if (!puzzle.isValidPoint(neighbourPoint))
			return null;

		EightPuzzle newPuzzle = puzzle.deepCopy();
		newPuzzle.swap(emptyPoint, neighbourPoint);

		return new EightPuzzleState(newPuzzle, neighbourPoint, puzzleMove);

	}



}