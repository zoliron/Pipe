package com.zoliron.games.maze;
//
//import com.zoliron.searchable.Searchable;
//import com.zoliron.searcher.SearcherNode;
//import com.zoliron.utils.MathUtils;
//
//import java.util.List;
//
//import test.Maze;
//
//
//
///**
// * The maze searchable.
// *
// * @author Ronen Zolicha
// */
public class MazeSearchable {//implements Searchable<MazeSearchableState>{
//
//
//
//	/**
//	 * The initial maze.
//	 */
//	private final Maze initial;
//
//
//
//	/**
//	 * Creates new {@link MazeSearchable} with the initial maze.
//	 */
//	public MazeSearchable(Maze initial){
//		this.initial = initial;
//	}
//
//
//
//	@Override
//	public MazeSearchableState getInitialState(){
//		return new MazeSearchableState(initial.getEntrance(), null);
//	}
//
//
//
//	@Override
//	public boolean isGoal(SearcherNode<MazeSearchableState> node){
//		return initial.getExit().equals(node.getState().getPoint());
//	}
//
//
//
//	@Override
//	public List<MazeSearchableState> getAllPossibleStates(MazeSearchableState state){
//		return null;
//	}
//
//
//
//	@Override
//	public double calculateCost(MazeSearchableState fromState, MazeSearchableState toState){
//		return 1d;
//	}
//
//
//
//	@Override
//	public double calculateEstimation(MazeSearchableState state){
//		int x1 = state.getPoint().col;
//		int y1 = state.getPoint().row;
//		int x2 = initial.getExit().col;
//		int y2 = initial.getExit().row;
//
//		return MathUtils.manhattanDistance(x1, y1, x2, y2);
//	}
//
//
//
}