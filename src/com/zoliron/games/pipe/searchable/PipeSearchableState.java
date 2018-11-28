package com.zoliron.games.pipe.searchable;

import com.zoliron.games.pipe.model.PipeBoard;
import com.zoliron.games.pipe.model.PipeCell;
import com.zoliron.utils.Point;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



/**
 * The {@link PipeSearchable} state.
 *
 * @author Ronen Zolicha
 */
public class PipeSearchableState{



	/**
	 * The state's board.
	 */
	private final PipeBoard board;



	/**
	 * The performed action which created this state.
	 */
	private final Point action;



	/**
	 * All reachable points from the board source point.
	 */
	private final Set<Point> reachablePointsFromSource;



	/**
	 * Creates new {@link PipeSearchableState} with the specified board and action.
	 */
	PipeSearchableState(PipeBoard board, Point action){
		this.board = board;
		this.action = action;
		this.reachablePointsFromSource = calculateReachablePointsFromSource(board);
	}



	/**
	 * Returns the state's board.
	 */
	public PipeBoard getBoard(){
		return board;
	}



	/**
	 * Returns the performed action which created this state.
	 */
	public Point getAction(){
		return action;
	}



	/**
	 * Returns all the reachable points from the board source point.
	 */
	public Set<Point> getReachablePointsFromSource(){
		return reachablePointsFromSource;
	}



	@Override
	public String toString(){
		return "Action=" + action
				+ "\nBoard=[\n" + board
				+ "\n]";
	}



	@Override
	public int hashCode(){
		return Objects.hash(board);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof PipeSearchableState))
			return false;

		PipeSearchableState other = (PipeSearchableState)obj;
		return Objects.equals(board, other.board);
	}



	/**
	 * Calculate all the reachable points from the board's source point.
	 */
	private static Set<Point> calculateReachablePointsFromSource(PipeBoard board){
		Set<Point> reachablePointsFromSource = new HashSet<>();

		Point source = board.getSource().getCoordinates();
		calculateReachablePoints(board, source, reachablePointsFromSource);

		return reachablePointsFromSource;
	}



	/**
	 * Calculate all the reachable points from the specified point.
	 */
	private static void calculateReachablePoints(PipeBoard board, Point p, Set<Point> reachablePoints){
		PipeCell cell = board.get(p);
		if (cell == null)
			return;

		// Mark the point as reachable.
		reachablePoints.add(p);

		// Discover from neighbors.
		discoverReachablePoint(board, p, cell.getLeft(), reachablePoints);
		discoverReachablePoint(board, p, cell.getTop(), reachablePoints);
		discoverReachablePoint(board, p, cell.getRight(), reachablePoints);
		discoverReachablePoint(board, p, cell.getBottom(), reachablePoints);
	}



	/**
	 * Discover if the specified {@param candidatePoint} is reachable from the {@param reachablePoint}.
	 */
	private static void discoverReachablePoint(PipeBoard board, Point reachablePoint, Point candidatePoint, Set<Point> reachablePoints){
		// Case 0: The candidate does not connected to the reachable point.
		if (!board.isConnected(reachablePoint, candidatePoint))
			return;

		// Case 1: Already discovered the candidate point.
		if (reachablePoints.contains(candidatePoint))
			return;

		// Case 2: Candidate point is reachable, calculate all reachable points for it.
		calculateReachablePoints(board, candidatePoint, reachablePoints);
	}



}
