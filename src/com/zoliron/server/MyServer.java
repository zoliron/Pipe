package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;



/**
 * The {@link Server} implementation.
 *
 * @author Ronen Zolicha
 */
public class MyServer implements Server{



	/**
	 * The server port.
	 */
	private final int port;



	/**
	 * The {@link ClientHandler}.
	 */
	private ClientHandler clientHandler;



	/**
	 * Indicates whether the server is stopped.
	 */
	private volatile boolean stop;



	/**
	 * Creates new {@link MyServer} with the specified port.
	 */
	public MyServer(int port){
		this.port = port;
	}



	@Override
	public void start(ClientHandler clientHandler){
		this.clientHandler = clientHandler;
		this.stop = false;

		new Thread(() -> {
			try{
				runServer();
			} catch (Exception e){
				e.printStackTrace();
			}
		}).start();
	}



	@Override
	public void stop(){
		this.clientHandler = null;
		this.stop = true;
	}



	/**
	 * Run & listen on the server port.
	 */
	private void runServer() throws Exception{
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);

		System.out.println("Waiting for clients on port: " + port);
		while (!stop){
			try{
				Socket socket = server.accept();
				try{
					InputStream inFromClient = socket.getInputStream();
					OutputStream outToClient = socket.getOutputStream();
					clientHandler.handleClient(inFromClient, outToClient);

					inFromClient.close();
					outToClient.close();
					socket.close();
				} catch (Exception e){
					e.printStackTrace();
				}

			} catch (SocketTimeoutException e){
				e.printStackTrace();
			}
		}

		server.close();
		System.out.println("Server closed!");
	}



}