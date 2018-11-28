package com.zoliron.games.pipe.model;

import com.zoliron.utils.Point;

import java.util.Objects;



/**
 * Encapsulate cell within {@link PipeBoard}.
 *
 * @author Ronen Zolicha
 */
public abstract class PipeCell{



	/**
	 * The pipe cell coordinates.
	 */
	private final Point coordinates;



	/**
	 * Creates new {@link PipeCell} with the specified coordinates.
	 */
	public PipeCell(Point coordinates){
		this.coordinates = coordinates;
	}



	/**
	 * Returns the pipe cell coordinates.
	 */
	public Point getCoordinates(){
		return coordinates;
	}



	/**
	 * Returns whether the pipe cell is the source cell.
	 */
	public boolean isSource(){
		return false;
	}



	/**
	 * Returns whether the pipe cell is the goal cell.
	 */
	public boolean isGoal(){
		return false;
	}



	/**
	 * Returns {@code true} if the cell can be rotated, otherwise returns {@code false}.
	 */
	public boolean isRotateable(){
		return false;
	}



	/**
	 * Rotate the cell (clockwise).
	 */
	public void rotate(){
		throw new UnsupportedOperationException("Unimplemented method!");
	}



	/**
	 * Walk through the given point.
	 */
	public abstract boolean isConnectedWith(PipeCell cell);



	/**
	 * Returns deep copy of this pipe cell.
	 */
	public abstract PipeCell deepCopy();



	/**
	 * Returns whether the specified point it to top of the cell coordinates.
	 */
	public final boolean isTopOf(Point point){
		return (coordinates.x == point.x) && (coordinates.y == point.y - 1);
	}



	/**
	 * Returns the point to top of the cell coordinates.
	 */
	public final Point getTop(){
		return new Point(coordinates.x, coordinates.y - 1);
	}



	/**
	 * Returns whether the specified point it to bottom of the cell coordinates.
	 */
	public final boolean isBottomOf(Point point){
		return (coordinates.x == point.x) && (coordinates.y == point.y + 1);
	}



	/**
	 * Returns the point to bottom of the cell coordinates.
	 */
	public final Point getBottom(){
		return new Point(coordinates.x, coordinates.y + 1);
	}



	/**
	 * Returns whether the specified point it to left of the cell coordinates.
	 */
	public final boolean isLeftOf(Point point){
		return (coordinates.x == point.x - 1) && (coordinates.y == point.y);
	}



	/**
	 * Returns the point to left of the cell coordinates.
	 */
	public final Point getLeft(){
		return new Point(coordinates.x - 1, coordinates.y);
	}



	/**
	 * Returns whether the specified point it to right of the cell coordinates.
	 */
	public final boolean isRightOf(Point point){
		return (coordinates.x == point.x + 1) && (coordinates.y == point.y);
	}



	/**
	 * Returns the point to right of the cell coordinates.
	 */
	public final Point getRight(){
		return new Point(coordinates.x + 1, coordinates.y);
	}



	@Override
	public int hashCode(){
		return Objects.hash(getClass(), coordinates);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof PipeCell))
			return false;

		PipeCell other = (PipeCell)obj;
		return Objects.equals(getClass(), other.getClass()) && Objects.equals(coordinates, other.coordinates);
	}



}