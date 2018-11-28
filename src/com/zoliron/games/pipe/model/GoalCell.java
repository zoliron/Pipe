package com.zoliron.games.pipe.model;

import com.zoliron.utils.Point;



/**
 * A goal {@link PipeCell}.
 *
 * @author Ronen Zolicha
 */
public class GoalCell extends PipeCell{



	/**
	 * Creates new {@link GoalCell} with the specified coordinates.
	 */
	public GoalCell(Point coordinates){
		super(coordinates);
	}



	@Override
	public boolean isGoal(){
		return true;
	}



	@Override
	public boolean isConnectedWith(PipeCell cell){
		return true;
	}



	@Override
	public String toString(){
		return "g";
	}



	@Override
	public PipeCell deepCopy(){
		return new GoalCell(getCoordinates());
	}



}