package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * The {@link Server} implementation.
 *
 * @author Ronen Zolicha
 */
public class MyServer implements Server{



	private static final Comparator<Runnable> COMPARATOR = new Comparator<>(){

		@Override
		public int compare(Runnable lhs, Runnable rhs){
			int lhsPriority = calculatePriority(lhs);
			int rhsPriority = calculatePriority(rhs);

			return Integer.compare(lhsPriority, rhsPriority);
		}



		private int calculatePriority(Runnable task){
			if (task instanceof Job){
				Job job = (Job)task;

				return job.getPriority();
			}

			return Integer.MIN_VALUE;
		}

	};



	/**
	 * The server port.
	 */
	private final int port;



	/**
	 * The server maximum concurrency.
	 */
	private final int poolSize;



	/**
	 * The executor.
	 */
	private ExecutorService executor;



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
	public MyServer(int port, int poolSize){
		this.port = port;
		this.poolSize = poolSize;
	}



	@Override
	public void start(ClientHandler clientHandler){
		this.clientHandler = clientHandler;
		this.executor = new ThreadPoolExecutor(poolSize, poolSize, 60L, TimeUnit.SECONDS, new PriorityBlockingQueue<>(50, COMPARATOR));
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
		shutdown();

		this.clientHandler = null;
		this.stop = true;
	}



	private void shutdown(){
		this.executor.shutdown();

		try{
			this.executor.awaitTermination(5, TimeUnit.SECONDS);
		}catch (Exception e){
			e.printStackTrace();
		}

		this.executor = null;
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

				executor.execute(new Reader(socket, clientHandler, executor));
			} catch (SocketTimeoutException e){
//				e.printStackTrace();
			}
		}

		server.close();
		System.out.println("Server closed!");
	}



}