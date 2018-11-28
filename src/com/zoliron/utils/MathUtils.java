package com.zoliron.utils;

/**
 * @author Ronen Zolicha
 */
public class MathUtils{



	/**
	 * Returns the euclidean distance between the two specified points on a 2D dimension.
	 */
	public double euclideanDistance(Point p1, Point p2){
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;

		return Math.sqrt(dx * dx + dy * dy);
	}



	/**
	 * Returns the manhattan distance between the two specified points on a 2D dimension.
	 */
	public static double manhattanDistance(Point p1, Point p2){
		return manhattanDistance(p1.x, p1.y, p2.x, p2.y);
	}



	/**
	 * Returns the manhattan distance between the two specified points on a 2D dimension.
	 */
	public static double manhattanDistance(int x1, int y1, int x2, int y2){
		double dx = x2 - x1;
		double dy = y2 - y1;

		return Math.abs(dx) + Math.abs(dy);
	}



}