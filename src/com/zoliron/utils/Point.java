package com.zoliron.utils;

import java.util.Objects;



/**
 * Encapsulate 2D point.
 *
 * @author Ronen Zolicha
 */
public class Point{



	/**
	 * The x-axis point.
	 */
	public final int x;



	/**
	 * The y-axis point.
	 */
	public final int y;



	/**
	 * Create new {@link Point} with the specified x and y coordinates.
	 */
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}



	@Override
	public int hashCode(){
		return Objects.hash(x, y);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Point))
			return false;

		Point other = (Point)obj;
		return (x == other.x) && (y == other.y);
	}



	@Override
	public String toString(){
		return "Point[x=" + x + ", y=" + y + "]";
	}



}
