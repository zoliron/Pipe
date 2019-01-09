package com.zoliron.games.pipe.searchable;

import com.zoliron.games.pipe.model.PipeBoard;
import com.zoliron.games.pipe.model.PipeCell;
import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.utils.MathUtils;
import com.zoliron.utils.Point;

import java.util.ArrayList;
import java.util.List;



/**
 * The pipe searchable.
 *
 * @author Ronen Zolicha
 */
public class PipeSearchable implements Searchable<PipeSearchableState>{



	/**
	 * The initial pipeGameBoard.
	 */
	private final PipeBoard initial;



	/**
	 * Creates new {@link PipeSearchable} with the initial pipeGameBoard.
	 */
	public PipeSearchable(PipeBoard initial){
		this.initial = initial;
	}



	@Override
	public PipeSearchableState getInitialState(){
		return new PipeSearchableState(initial, null);
	}



	@Override
	public boolean isGoal(SearcherNode<PipeSearchableState> node){
		return node.getEstimation() == 0d;
	}



	@Override
	public List<PipeSearchableState> getAllPossibleStates(PipeSearchableState state){
		PipeBoard current = state.getBoard();

		int rowCount = current.getRowCount();
		int columnCount = current.getColumnCount();
		List<PipeSearchableState> possibleStates = new ArrayList<>(rowCount * columnCount);
		for (int y = 0; y < rowCount; y++)
			for (int x = 0; x < columnCount; x++){
				PipeCell cell = current.get(x, y);
				if (!cell.isRotateable())
					continue;

				PipeBoard copy = new PipeBoard(current);

				PipeCell copyCell = copy.get(x, y);
				copyCell.rotate();

				possibleStates.add(new PipeSearchableState(copy, copyCell.getCoordinates()));
			}

		return possibleStates;
	}



	@Override
	public double calculateCost(PipeSearchableState fromState, PipeSearchableState toState){
		return (fromState == null) ? 0d : 1d; // All actions with the same cost.
	}



	@Override
	public double calculateEstimation(PipeSearchableState state){
		Point source = state.getBoard().getSource().getCoordinates();
		Point goal = state.getBoard().getGoal().getCoordinates();

		double minDistance = MathUtils.manhattanDistance(source, goal);
		for (Point p : state.getReachablePointsFromSource()){
			minDistance = Math.min(
					MathUtils.manhattanDistance(p, goal),
					minDistance);
		}

		return minDistance;
	}



}