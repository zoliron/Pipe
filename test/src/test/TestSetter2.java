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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class TestSetter2{



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
		MazeSearchable test = new MazeSearchable(m);
		Solution<MazeSearchableState> solution = new BestFirstSearch<MazeSearchableState>().search(test);

		if (solution == null)
			return new ArrayList<>();

		List<String> resault = new ArrayList<>();
		List<MazeSearchableState> solutionStates = solution.getStates();

		for (int i = 0; i < solutionStates.size() - 1; i++){
			MazeSearchableState from = solutionStates.get(i);
			MazeSearchableState to = solutionStates.get(i + 1);
			String direction = test.getDirection(from.spot, to.spot);
			resault.add(direction);
		}
		System.out.println(resault);
		return resault;

	}



	private static class MazeSearchableState{



		private final Grid spot;



		private MazeSearchableState(Grid spot){
			this.spot = spot;
		}



		public Grid getSpot(){
			return spot;
		}



		@Override
		public boolean equals(Object obj){
			if (!(obj instanceof MazeSearchableState))
				return false;

			MazeSearchableState other = (MazeSearchableState)obj;
			return (spot.col == other.spot.col && spot.row == other.spot.row);
		}



		@Override
		public int hashCode(){
			return Objects.hash(spot.row, spot.col);
		}


	}



	private static class MazeSearchable implements Searchable<MazeSearchableState>{



		private final Maze initialMaze;



		private MazeSearchable(Maze initialMaze){
			this.initialMaze = initialMaze;
		}



		@Override
		public MazeSearchableState getInitialState(){
			return new MazeSearchableState(initialMaze.getEntrance());
		}



		@Override
		public boolean isGoal(SearcherNode<MazeSearchableState> node){
			MazeSearchableState state = node.getState();
			MazeSearchableState exitState = new MazeSearchableState(initialMaze.getExit());

			return state.equals(exitState);
		}



		@Override
		public List<MazeSearchableState> getAllPossibleStates(MazeSearchableState state){
			List<Grid> spots = initialMaze.getPossibleMoves(state.spot);
			List<MazeSearchableState> resault = new ArrayList<>();

			Grid cur = state.spot;
			for (Grid spot : spots){
				if (isValid(cur, spot))
					resault.add(new MazeSearchableState(spot));
			}
			if (isValid(cur, initialMaze.getExit()))
				resault.add(new MazeSearchableState(initialMaze.getExit()));

			return resault;
		}


		private boolean isValid (Grid from, Grid to){
			return getDirection(from, to) != null;
		}

		public String getDirection(Grid from, Grid to){
			//Up
			if (to.row == from.row - 1 && to.col == from.col){
				return "UP";
			}
			//Down
			else if (to.row == from.row + 1 && to.col == from.col){
				return "DOWN";
			}
			//Right
			else if (to.col == from.col + 1 && to.row == from.row){
				return "RIGHT";
			}
			//Left
			else if (to.col == from.col - 1 && to.row == from.row){
				return "LEFT";
			}
			return null;
		}



		@Override
		public double calculateCost(MazeSearchableState fromState, MazeSearchableState toState){
			return 1d;
		}



		@Override
		public double calculateEstimation(MazeSearchableState state){
			return 0d;
		}
	}



}