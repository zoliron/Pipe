package com.zoliron.server;



import com.zoliron.client.ClientHandler;



/**
 * The server interface.
 *
 * @author Ronen Zolicha
 */
public interface Server{



	/**
	 * Starts the server with the specified {@link ClientHandler}.
	 */
	void start(ClientHandler clientHandler);



	/**
	 * Stops the server.
	 */
	void stop();



}