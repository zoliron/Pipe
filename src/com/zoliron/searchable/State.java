package com.zoliron.searchable;

import java.util.Objects;



/**
 * @author Ronen Zolicha
 */
public class State<T>{



	/**
	 * The state object.
	 */
	private final T stateObject;



	/**
	 * The state we came from.
	 */
	private State<T> cameFrom;



	/**
	 * Creates new {@link State} with the specified object.
	 */
	public State(T stateObject){
		this.stateObject = stateObject;
		this.cameFrom = null;
	}



	/**
	 * Returns the state object.
	 */
	public T getStateObject(){
		return stateObject;
	}



	/**
	 * Returns the state which we came from to this state.
	 */
	public State<T> getCameFrom(){
		return cameFrom;
	}



	/**
	 * Sets the state which we came from to this state.
	 */
	public void setCameFrom(State<T> cameFrom){
		this.cameFrom = cameFrom;
	}



	@Override
	public int hashCode(){
		return Objects.hashCode(stateObject);
	}



	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof State))
			return false;

		State<?> other = (State<?>)obj;
		return Objects.equals(stateObject, other.stateObject);
	}



}