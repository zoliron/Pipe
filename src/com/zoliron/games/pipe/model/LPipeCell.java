package com.zoliron.games.pipe.model;

import com.zoliron.utils.Point;



/**
 * A 'L' or 'F' or '7' or 'J' {@link PipeCell}.
 *
 * @author Ronen Zolicha
 */
public class LPipeCell extends DegreesPipeCell{



	/**
	 * Creates new {@link LPipeCell} with the specified coordinates and degrees.
	 */
	public LPipeCell(Point coordinates, int degrees){
		super(coordinates, degrees);
	}



	@Override
	public final void rotate(){
		this.degrees = (degrees + 90) % 360;
	}



	@Override
	public boolean isConnectedWith(PipeCell cell){
		Point point = cell.getCoordinates();

		switch (degrees){
			case 0: // 'L' case
				return isBottomOf(point) || isLeftOf(point);

			case 90: // 'F' case
				return isTopOf(point) || isLeftOf(point);

			case 180: // '7' case
				return isTopOf(point) || isRightOf(point);

			case 270: // 'J' case
				return isBottomOf(point) || isRightOf(point);

			default:
				throw new IllegalStateException("Unsupported degrees: " + degrees);
		}
	}



	@Override
	public String toString(){
		switch (degrees){
			case 0: // 'L' case
				return "L";

			case 90: // 'F' case
				return "F";

			case 180: // '7' case
				return "7";

			case 270: // 'J' case
				return "J";

			default:
				throw new IllegalStateException("Unsupported degrees: " + degrees);
		}
	}



	@Override
	public PipeCell deepCopy(){
		return new LPipeCell(getCoordinates(), degrees);
	}


}
