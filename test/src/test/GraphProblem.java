package test;

import com.zoliron.searchable.Searchable;
import com.zoliron.searcher.SearcherNode;
import com.zoliron.searcher.Solution;
import com.zoliron.searcher.algorithms.BestFirstSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import test.hipster.graph.GraphBuilder;
import test.hipster.graph.GraphEdge;
import test.hipster.graph.HipsterDirectedGraph;
import test.hipster.graph.HipsterGraph;



/**
 * @author Ronen Zolicha
 */
public class GraphProblem{



	public static void main(String[] args){
		HipsterDirectedGraph<String, Double> graph = GraphBuilder.<String, Double>create()
				.connect("A").to("B").withEdge(4.0D)
				.connect("A").to("C").withEdge(2.0D)
				.connect("B").to("C").withEdge(5.0D)
				.connect("B").to("D").withEdge(10.0D)
				.connect("C").to("E").withEdge(3.0D)
				.connect("D").to("F").withEdge(11.0D)
				.connect("E").to("D").withEdge(4.0D)
				.createDirectedGraph();

		System.out.println("Path: " + solveGraph(graph, "A", "F"));
	}



	private static List<String> solveGraph(HipsterGraph<String, Double> graph, String fromVertex, String toVertex){

		GraphSearchable searchable = new GraphSearchable(graph, fromVertex, toVertex);
		Solution<GraphSearchableState> solution = new BestFirstSearch<GraphSearchableState>().search(searchable);

		if (solution == null)
			return new ArrayList<>();

//		List<String> shortestPath = new ArrayList<>();
//		for (GraphSearchableState graphSearchableState : solution){
//			shortestPath.add(graphSearchableState.vertex);
//		}
//
//		return shortestPath;
		System.out.println("Cost: " + solution.getCost());
		return solution.getStates().stream()
				.map(s -> s.vertex)
				.collect(Collectors.toList());
	}



	private static class GraphSearchableState{



		private final String vertex;



		private GraphSearchableState(String vertex){
			this.vertex = vertex;
		}



		@Override
		public int hashCode(){
			return Objects.hashCode(vertex);
		}



		@Override
		public boolean equals(Object obj){
			if (!(obj instanceof GraphSearchableState))
				return false;

			GraphSearchableState other = (GraphSearchableState)obj;
			return Objects.equals(vertex, other.vertex);
		}
	}



	private static class GraphSearchable implements Searchable<GraphSearchableState>{



		private final HipsterGraph<String, Double> graph;



		private final String source;



		private final String goal;



		private GraphSearchable(HipsterGraph<String, Double> graph, String source, String goal){
			this.graph = graph;
			this.source = source;
			this.goal = goal;
		}



		@Override
		public GraphSearchableState getInitialState(){
			return new GraphSearchableState(source);
		}



		@Override
		public boolean isGoal(SearcherNode<GraphSearchableState> node){
			GraphSearchableState state = node.getState();
			GraphSearchableState exitState = new GraphSearchableState(goal);

			return state.equals(exitState);
		}



		@Override
		public List<GraphSearchableState> getAllPossibleStates(GraphSearchableState state){
			List<GraphSearchableState> result = new ArrayList<>();

			Iterable<GraphEdge<String, Double>> edges = graph.edgesOf(state.vertex);
			for (GraphEdge<String, Double> edge : edges){
				if (edge.getVertex1().equals(state.vertex)) // Outgoing edge.
					result.add(new GraphSearchableState(edge.getVertex2()));
			}

			return result;
		}



		@Override
		public double calculateCost(GraphSearchableState fromState, GraphSearchableState toState){
			Iterable<GraphEdge<String, Double>> fromEdges = graph.edgesOf(fromState.vertex);
			for (GraphEdge<String, Double> fromEdge : fromEdges){
				if (fromEdge.getVertex2().equals(toState.vertex)){  // Incoming edge.
					return fromEdge.getEdgeValue();
				}
			}

			return Double.MAX_VALUE;
		}



		@Override
		public double calculateEstimation(GraphSearchableState state){
			return 0;
		}
	}


}