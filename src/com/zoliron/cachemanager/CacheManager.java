package com.zoliron.cachemanager;

import java.io.IOException;



/**
 * The games cache manager.
 *
 * @author Ronen Zolicha
 */
public interface CacheManager{



	/**
	 * Save the specified solution for the given problem.
	 */
	void save(String problem, String solution) throws IOException;



	/**
	 * Return the cached solution for the specified problem.
	 */
	String load(String problem) throws IOException;



}