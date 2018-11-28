package com.zoliron.games.pipe.model;

import com.zoliron.utils.Point;

import java.util.Objects;



/**
 * A degrees rotateable {@link PipeCell}.
 *
 * @author Ronen Zolicha
 */
public abstract class DegreesPipeCell extends PipeCell{



	/**
	 * The rotation degrees.
	 */
	protected int degrees;



	/**
	 * Creates new {@link DegreesPipeCell} with the specified coordinates and degrees.
	 */
	public DegreesPipeCell(Point coordinates, int degrees){
		super(coordinates);

		this.degrees = degrees;
	}



	@Override
	public boolean isRotateable(){
		return true;
	}



	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), degrees);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof DegreesPipeCell))
			return false;

		DegreesPipeCell other = (DegreesPipeCell)obj;
		return super.equals(other) && (degrees == other.degrees);
	}



}