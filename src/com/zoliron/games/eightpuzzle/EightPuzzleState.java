package com.zoliron.games.eightpuzzle;

import com.zoliron.games.eightpuzzle.EightPuzzle.PuzzleMove;
import com.zoliron.utils.Point;

import java.util.Objects;



/**
 * The {@link EightPuzzleSearchable} state.
 *
 * @author Ronen Zolicha
 */
public class EightPuzzleState{



	/**
	 * The state puzzle.
	 */
	private final EightPuzzle puzzle;



	/**
	 * The state puzzle point moved.
	 */
	private final Point point;



	/**
	 * The state puzzle point move.
	 */
	private final PuzzleMove move;



	/**
	 * Creates new {@link EightPuzzleState} with the specified parameters.
	 */
	public EightPuzzleState(EightPuzzle puzzle, Point point, PuzzleMove move){
		this.puzzle = puzzle;
		this.point = point;
		this.move = move;
	}



	/**
	 * Returns the state puzzle.
	 */
	public EightPuzzle getPuzzle(){
		return puzzle;
	}



	/**
	 * Returns the state puzzle point moved.
	 */
	public Point getPoint(){
		return point;
	}



	/**
	 * Returns the state puzzle point move.
	 */
	public PuzzleMove getMove(){
		return move;
	}



	@Override
	public String toString(){
		return "Move " + point + " - " + move;
	}



	@Override
	public int hashCode(){
		return Objects.hash(puzzle);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof EightPuzzleState))
			return false;

		EightPuzzleState other = (EightPuzzleState)obj;
		return Objects.equals(puzzle, other.puzzle);
	}


}
