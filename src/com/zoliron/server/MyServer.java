package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;


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

    BlockingQueue<Runnable> priorityQueue;
    int clientsNumber;
    ExecutorService threadPool;


	/**
	 * Creates new {@link MyServer} with the specified port.
	 */
	public MyServer(int port, int clientsNumber){
		this.port = port;
		this.clientsNumber = clientsNumber;
	}



	@Override
	public void start(ClientHandler clientHandler){
		this.clientHandler = clientHandler;
		this.stop = false;
        priorityQueue = new PriorityBlockingQueue<>();
        threadPool = new ThreadPoolExecutor(clientsNumber, clientsNumber, 0L, TimeUnit.MILLISECONDS, priorityQueue);

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
//		threadPool.awaitTermination(0L,TimeUnit.MILLISECONDS);
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
//					InputStream inFromClient = socket.getInputStream();
//					OutputStream outToClient = socket.getOutputStream();
//					clientHandler.handleClient(inFromClient, outToClient);
                    Job newJob = new Job(socket, clientHandler);
                    threadPool.execute(newJob);
//					inFromClient.close();
//					outToClient.close();
//					socket.close();
				} catch (Exception e){
					e.printStackTrace();
				}

			} catch (SocketTimeoutException e){
//				e.printStackTrace();
			}
		}

		server.close();
		System.out.println("Server closed!");
	}



}