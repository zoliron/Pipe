package com.zoliron.games.eightpuzzle;

import com.zoliron.utils.Point;

import java.util.Arrays;
import java.util.Iterator;



/**
 * @author Ronen Zolicha
 */
public class EightPuzzle implements Iterable<Point>{



	/**
	 * The goal puzzle.
	 */
	public static final EightPuzzle GOAL = new EightPuzzle(new String[][]{{" ", "1", "2"}, {"3", "4", "5"}, {"6", "7", "8"}});



	/**
	 * The row count.
	 */
	private static final int ROW_COUNT = 3;



	/**
	 * The column.
	 */
	private static final int COLUMN_COUNT = 3;



	/**
	 * The puzzle pipeGameBoard.
	 */
	private final String[][] board;



	/**
	 * Creates new {@link EightPuzzle} with the specified pipeGameBoard.
	 */
	public EightPuzzle(String[][] board){
		this.board = board;
	}



	/**
	 * Returns the tile of the specified point.
	 */
	public String getTile(Point p){
		if (!isValidPoint(p))
			throw new IllegalStateException("Invalid point: " + p);

		return board[p.x][p.y];
	}



	/**
	 * Returns the puzzle empty point.
	 */
	public Point getEmptyPoint(){
		return getPoint(" ");
	}



	/**
	 * Find the puzzle point for the specified tile.
	 */
	public Point getPoint(String tile){
		for (int y = 0; y < ROW_COUNT; y++){
			for (int x = 0; x < COLUMN_COUNT; x++){
				if (tile.equals(board[y][x]))
					return new Point(x, y);
			}
		}

		throw new IllegalStateException("Unsupported tile: " + tile);
	}



	/**
	 * Returns {@code true} if the specified point within the puzzle, otherwise returns {@code false}.
	 */
	public boolean isValidPoint(Point p){
		return (0 <= p.x && p.x < COLUMN_COUNT) && (0 <= p.y && p.y < ROW_COUNT);
	}



	/**
	 * Resolve the point we need to perform the action upon.
	 */
	public Point getPointForAction(Point empty, PuzzleMove puzzleMove){
		switch (puzzleMove){
			case UP:
				return new Point(empty.x, empty.y + 1);  // Move bottom point top.
			case DOWN:
				return new Point(empty.x, empty.y - 1); // Move top point bottom.
			case LEFT:
				return new Point(empty.x + 1, empty.y);  // Move right point left.
			case RIGHT:
				return new Point(empty.x - 1, empty.y); // Move left point right.
			default:
				throw new IllegalStateException("Unknown action: " + puzzleMove);
		}
	}



	/**
	 * Swaps between the 2 points.
	 */
	public void swap(Point p1, Point p2){
		String tmp = board[p1.y][p1.x];
		board[p1.y][p1.x] = board[p2.y][p2.x];
		board[p2.y][p2.x] = tmp;
	}



	/**
	 * Deep copy the specified pipeGameBoard.
	 */
	public EightPuzzle deepCopy(){
		String[][] copy = new String[COLUMN_COUNT][ROW_COUNT];

		for (int y = 0; y < ROW_COUNT; y++)
			for (int x = 0; x < COLUMN_COUNT; x++)
				copy[y][x] = board[y][x];

		return new EightPuzzle(copy);
	}



	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(ROW_COUNT * COLUMN_COUNT);

		for (int y = 0; y < ROW_COUNT; y++)
			for (int x = 0; x < COLUMN_COUNT; x++)
				sb.append(board[y][x]);

		return sb.toString();
	}



	@Override
	public int hashCode(){
		return Arrays.deepHashCode(board);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof EightPuzzle))
			return false;

		EightPuzzle other = (EightPuzzle)obj;
		return Arrays.deepEquals(board, other.board);
	}



	@Override
	public Iterator<Point> iterator(){
		return new EightPuzzleIterator();
	}



	/**
	 * The puzzle movements.
	 */
	public enum PuzzleMove{

		LEFT, RIGHT, UP, DOWN

	}



	/**
	 * The {@link EightPuzzle} iterator.
	 */
	private static class EightPuzzleIterator implements Iterator<Point>{



		/**
		 * The current x.
		 */
		private int x = 0;



		/**
		 * The current y.
		 */
		private int y = 0;



		@Override
		public boolean hasNext(){
			return x < COLUMN_COUNT && y < ROW_COUNT;
		}



		@Override
		public Point next(){
			Point next = new Point(x, y);

			if (x == COLUMN_COUNT - 1){
				x = 0;
				y++;
			} else
				x++;

			return next;
		}



	}



}
