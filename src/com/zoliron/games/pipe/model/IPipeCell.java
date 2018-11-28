package com.zoliron.games.pipe.model;

import com.zoliron.utils.Point;



/**
 * A '|' or '-' {@link PipeCell}.
 *
 * @author Ronen Zolicha
 */
public class IPipeCell extends DegreesPipeCell{



	/**
	 * Creates new {@link IPipeCell} with the specified coordinates and degrees.
	 */
	public IPipeCell(Point coordinates, int degrees){
		super(coordinates, degrees);
	}



	@Override
	public final void rotate(){
		this.degrees = (degrees == 0) ? 90 : 0;
	}



	@Override
	public boolean isConnectedWith(PipeCell cell){
		Point point = cell.getCoordinates();

		switch (degrees){
			// '|' case
			case 0:
				return isTopOf(point) || isBottomOf(point);

			// '-' case
			case 90:
				return isLeftOf(point) || isRightOf(point);

			default:
				throw new IllegalStateException("Unsupported degrees: " + degrees);
		}
	}



	@Override
	public String toString(){
		switch (degrees){
			// '|' case
			case 0:
				return "|";

			// '-' case
			case 90:
				return "-";

			default:
				throw new IllegalStateException("Unsupported degrees: " + degrees);
		}
	}



	@Override
	public PipeCell deepCopy(){
		return new IPipeCell(getCoordinates(), degrees);
	}


}