package com.zoliron.server;

import com.zoliron.client.ClientHandler;
import com.zoliron.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;



public class Job implements Runnable{



	private final Socket socket;
	private final ClientHandler clientHandler;
	private final byte[] input;



	Job(Socket socket, ClientHandler clientHandler, byte[] input){
		this.socket = socket;
		this.clientHandler = clientHandler;
		this.input = input;
	}



	@Override
	public void run(){
		try{
			InputStream inFromClient = new ByteArrayInputStream(input);
			OutputStream outToClient = socket.getOutputStream();
			clientHandler.handleClient(inFromClient, outToClient);
			inFromClient.close();
			outToClient.close();
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			IoUtils.safeClose(socket);
		}
	}



	/**
	 * Returns the job priority.
	 */
	int getPriority(){
		return input.length;
	}



}
