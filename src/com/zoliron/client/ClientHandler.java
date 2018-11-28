package com.zoliron.client;

import java.io.InputStream;
import java.io.OutputStream;



/**
 * The client request handler.
 *
 * @author Ronen Zolicha
 */
public interface ClientHandler{



	/**
	 * Handles the client request and post the response to the client.
	 */
	void handleClient(InputStream inFromClient, OutputStream outToClient);



}