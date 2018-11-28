package com.zoliron.tmp;

import com.zoliron.client.ClientHandler;
import com.zoliron.games.pipe.PipeBoardParser;
import com.zoliron.games.pipe.model.PipeBoard;
import com.zoliron.games.pipe.searchable.PipeSearchable;
import com.zoliron.games.pipe.searchable.PipeSearchableState;
import com.zoliron.searcher.Solution;
import com.zoliron.searcher.algorithms.AStar;

import java.io.*;



public class PrintHandler implements ClientHandler{



	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient){
		try{
			String rawBoard = readToEnd(inFromClient);

			PipeBoard board = new PipeBoardParser().parse(rawBoard);
			System.out.println(board);

			PipeSearchable p = new PipeSearchable(board);
//			System.out.println(p.getInitialState().getReachablePointsFromSource());
//			System.out.println("\n\n");

			long t, diff;

//			t = System.currentTimeMillis();
//			Solution<PipeSearchableState> s1 = new BFS<PipeSearchableState>().search(p);
//			diff = System.currentTimeMillis() - t;

//			System.out.println("BFS: Time=" + diff);
//			System.out.println();
//			System.out.println(s1);
//			System.out.println("\n\n");

//			t = System.currentTimeMillis();
//			Solution<PipeSearchableState> s2 = new BestFirstSearch<PipeSearchableState>().search(p);
//			diff = System.currentTimeMillis() - t;
//
//			System.out.println("BestFirstSearch: Time=" + diff);
//			System.out.println();
//			System.out.println(s2);
//			System.out.println("\n\n");

//			t = System.currentTimeMillis();
//			Solution<PipeSearchableState> s3 = new HillClimbing<PipeSearchableState>().search(p);
//			diff = System.currentTimeMillis() - t;
//
//			System.out.println("HillClimbing: Time=" + diff);
//			System.out.println();
//			System.out.println(s3);
//			System.out.println("\n\n");

			t = System.currentTimeMillis();
			Solution<PipeSearchableState> s4 = new AStar<PipeSearchableState>().search(p);
			diff = System.currentTimeMillis() - t;

			System.out.println("AStar: Time=" + diff);
			System.out.println();
			System.out.println(s4);
			System.out.println("\n\n");



//			List<State<PipeState>> states = p.getAllPossibleStates(p.getInitialState());
//			for (State<PipeState> state : states){
//				System.out.println("\n\n\n\n");
//				System.out.println(state.getStateObject().board);
//			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}



	/**
	 * Read the problem input until "done".
	 */
	private String readToEnd(InputStream inFromClient) throws IOException{
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
		String line;
		while ((line = br.readLine()) != null && !"done".equals(line))
			sb.append(line).append('\n');

		return sb.toString();
	}



}
