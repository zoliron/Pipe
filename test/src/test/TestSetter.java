package test;

import com.zoliron.cachemanager.CacheManager;
import com.zoliron.cachemanager.FileCacheManager;
import com.zoliron.client.ClientHandler;
import com.zoliron.client.MyClientHandler;
import com.zoliron.games.pipe.PipeSolver;
import com.zoliron.games.pipe.searchable.PipeSearchable;
import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.Searcher;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;
import com.zoliron.searcher.algorithms.BestFirstSearch;
import com.zoliron.server.MyServer;
import com.zoliron.server.Server;
import com.zoliron.solver.Solver;
import com.zoliron.utils.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class TestSetter{



	public static void setClasses(DesignTest dt){
		// set the server's Interface, e.g., "Server.class"
		// don't forget to import the correct package e.g., "import server.Server"
		dt.setServerInteface(Server.class);
		// now fill in the other types according to their names
		// server's implementation
		dt.setServerClass(MyServer.class);
		// client handler interface
		dt.setClientHandlerInterface(ClientHandler.class);
		// client handler class
		dt.setClientHandlerClass(MyClientHandler.class);
		// cache manager interface
		dt.setCacheManagerInterface(CacheManager.class);
		// cache manager class
		dt.setCacheManagerClass(FileCacheManager.class);
		// solver interface
		dt.setSolverInterface(Solver.class);
		// solver class
		dt.setSolverClass(PipeSolver.class);
		// searchable interface
		dt.setSearchableInterface(Searchable.class);
		// searcher interface
		dt.setSearcherInterface(Searcher.class);
		// your searchable pipe game class
		dt.setPipeGameClass(PipeSearchable.class);
		// your Best First Search implementation
		dt.setBestFSClass(BestFirstSearch.class);
	}



	// run your server here
	static Server s;



	public static void runServer(int port){
		s = new MyServer(port);
		s.start(new MyClientHandler());
	}



	// stop your server here
	public static void stopServer(){
		s.stop();
	}



	/* ------------- Best First Search Test --------------
	 * You are given a Maze
	 * Create a new Searchable from the Maze
	 * Solve the Maze
	 * and return a list of moves from {UP,DOWN,RIGHT,LEFT}
	 *
	 */
	public static List<String> solveMaze(Maze m){
//		Option 1
//		Searchable<Grid> searchable = new Searchable<Grid>(){
//
//			@Override
//			public Grid getInitialState(){
//				return m.getEntrance();
//			}
//
//
//
//			@Override
//			public boolean isGoal(SearcherNode<Grid> node){
//				return m.getExit().equals(node.getState());
//			}
//
//
//
//			@Override
//			public List<Grid> getAllPossibleStates(Grid state){
//				List<Grid> states = m.getPossibleMoves(state);
//
//				// Workaround for lecturer bugs.
//				int row = state.row;
//				int col = state.col;
//
//				// Remove the cross-line grids.
//				Iterator<Grid> it = states.iterator();
//				while (it.hasNext()){
//					Grid s = it.next();
//					if (Math.abs(row - s.row) + Math.abs(col - s.col) != 1)
//						it.remove();
//				}
//
//				// Add the goal.
//				Grid g = m.getExit();
//				if (Math.abs(row - g.row) + Math.abs(col - g.col) == 1)
//					states.add(g);
//
//				return states;
//			}
//
//
//
//			@Override
//			public double calculateCost(Grid fromState, Grid toState){
//				return 1d;
//			}
//
//
//
//			@Override
//			public double calculateEstimation(Grid state){
//				int x1 = state.col;
//				int y1 = state.row;
//				int x2 = m.getExit().col;
//				int y2 = m.getExit().row;
//
//				return MathUtils.manhattanDistance(x1, y1, x2, y2);
//			}
//
//		};
//
//		Solution<Grid> solution = new BestFirstSearch<Grid>().search(searchable);
//		List<Grid> moves = solution.getStates();

//		Option 2
		MazeSearchable mazeSearchable = new MazeSearchable(m);
		Solution<MazeSearchableState> solution = new BestFirstSearch<MazeSearchableState>().search(mazeSearchable);
		List<Grid> moves = solution.getStates().stream()
				.map(s -> s.point)
				.collect(Collectors.toList());

		List<String> result = new ArrayList<>();
		int size = moves.size();
		for (int i = 1; i < size; i++){
			Grid from = moves.get(i - 1);
			Grid to = moves.get(i);

			if (from.col > to.col)
				result.add("LEFT");
			else if (from.col < to.col)
				result.add("RIGHT");
			else if (from.row > to.row)
				result.add("UP");
			else if (from.row < to.row)
				result.add("DOWN");
			else
				throw new IllegalStateException("Unsupported Move!");
		}

		return result;
	}



	/**
	 * The maze searchable.
	 *
	 * Option 2
	 */
	private static class MazeSearchable implements Searchable<MazeSearchableState>{



		/**
		 * The initial maze.
		 */
		private final Maze initial;



		/**
		 * Creates new {@link MazeSearchable} with the initial maze.
		 */
		public MazeSearchable(Maze initial){
			this.initial = initial;
		}



		@Override
		public MazeSearchableState getInitialState(){
			return new MazeSearchableState(initial.getEntrance());
		}



		@Override
		public boolean isGoal(SearcherNode<MazeSearchableState> node){
			MazeSearchableState exit = new MazeSearchableState(initial.getExit());

			return exit.equals(node.getState());
		}



		@Override
		public List<MazeSearchableState> getAllPossibleStates(MazeSearchableState state){
			List<Grid> states = initial.getPossibleMoves(state.point);

			// Workaround for lecturer bugs.
			int row = state.point.row;
			int col = state.point.col;

			// Remove the cross-line grids.
			Iterator<Grid> it = states.iterator();
			while (it.hasNext()){
				Grid s = it.next();
				if (Math.abs(row - s.row) + Math.abs(col - s.col) != 1)
					it.remove();
			}

			// Add the goal.
			Grid g = initial.getExit();
			if (Math.abs(row - g.row) + Math.abs(col - g.col) == 1)
				states.add(g);

			return states.stream()
					.map(MazeSearchableState::new)
					.collect(Collectors.toList());
		}



		@Override
		public double calculateCost(MazeSearchableState fromState, MazeSearchableState toState){
			return 1d;
		}



		@Override
		public double calculateEstimation(MazeSearchableState state){
			int x1 = state.getPoint().col;
			int y1 = state.getPoint().row;
			int x2 = initial.getExit().col;
			int y2 = initial.getExit().row;

			return MathUtils.manhattanDistance(x1, y1, x2, y2);
		}



	}



	/**
	 * The maze searchable state.
	 *
	 * Option 2
	 */
	private static class MazeSearchableState{



		/**
		 * The state point moved.
		 */
		private final Grid point;



		/**
		 * Creates new {@link MazeSearchableState} with the specified parameters.
		 */
		public MazeSearchableState(Grid point){
			this.point = point;
		}



		/**
		 * Returns the state maze point moved.
		 */
		public Grid getPoint(){
			return point;
		}



		@Override
		public String toString(){
			return "Point " + point;
		}



		@Override
		public int hashCode(){
			return Objects.hash(point.col, point.row);
		}



		@Override
		public boolean equals(Object obj){
			if (!(obj instanceof MazeSearchableState))
				return false;

			MazeSearchableState other = (MazeSearchableState)obj;
			return point.col == other.point.col && point.row == other.point.row;
		}



	}



}