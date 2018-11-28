package com.zoliron.searcher;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



/**
 * Encapsulate the {@link Searcher} solution.
 */
public class Solution<S> implements Iterable<S>{



	/**
	 * The solution states.
	 */
	private final List<S> states;



	/**
	 * The solution cost.
	 */
	private final double cost;



	/**
	 * The solution total number of iterations.
	 */
	private final int numOfIteration;



	/**
	 * The solution duration, in milliseconds.
	 */
	private final Duration duration;



	/**
	 * Creates new {@link Solution} with the specified cost and states.
	 */
	Solution(List<S> states, double cost, int numOfIteration, Duration duration){
		this.states = Collections.unmodifiableList(states);
		this.cost = cost;
		this.numOfIteration = numOfIteration;
		this.duration = duration;
	}



	/**
	 * Returns the solution states.
	 */
	public List<S> getStates(){
		return states;
	}



	/**
	 * Returns the solution last state.
	 */
	public S getLastState(){
		return states.get(states.size() - 1);
	}



	/**
	 * Returns the solution cost.
	 */
	public double getCost(){
		return cost;
	}



	/**
	 * Returns the solution total number of iterations.
	 */
	public int getNumOfIteration(){
		return numOfIteration;
	}



	/**
	 * Returns the solution duration, in milliseconds.
	 */
	public Duration getDuration(){
		return duration;
	}



	@Override
	public Iterator<S> iterator(){
		return states.iterator();
	}



	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("Cost: ").append(cost).append("\n");
		sb.append("Iterations: ").append(numOfIteration).append("\n");
		sb.append("Duration: ").append(duration.getSeconds()).append(" seconds\n");
		sb.append("Final:\n").append(getLastState()).append("\n");

		return sb.toString();
	}

}