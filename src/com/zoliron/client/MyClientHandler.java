package com.zoliron.client;

import com.zoliron.cachemanager.CacheManager;
import com.zoliron.cachemanager.FileCacheManager;
import com.zoliron.games.pipe.PipeSolver;
import com.zoliron.games.pipe.model.PipeBoard;
import com.zoliron.solver.Solver;

import java.io.*;



/**
 * The {@link ClientHandler} implementation.
 *
 * @author Ronen Zolicha
 */
public class MyClientHandler implements ClientHandler{



	/**
	 * The client handler cache manager.
	 */
	private final CacheManager cm = new FileCacheManager();



	/**
	 * The solver.
	 */
	private final Solver<PipeBoard> solver = new PipeSolver();



	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient){
		try{
			String problem = readToEnd(inFromClient);

			System.out.println("Problem:");
			System.out.println(problem);

			String solution = solve(solver, problem);

			System.out.println("Solution:");
			System.out.println(solution);

			sendSolution(solution, outToClient);
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



	/**
	 * Solve and returns the specified problem with the given solver.
	 */
	private <P> String solve(Solver<P> solver, String problem) throws IOException{
		String solution = cm.load(problem);
		if (solution != null)
			return solution;

		P solverProblem = solver.createProblem(problem);

		solution = solver.solve(solverProblem);
		if (solution != null)
			cm.save(problem, solution);

		return solution;
	}



	/**
	 * Send the specified solution to the client.
	 */
	private void sendSolution(String solution, OutputStream os) throws IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

		if (solution != null && !solution.isEmpty()){
			bw.write(solution);
			bw.newLine();
		}

		bw.write("done");
		bw.flush();
	}



}