package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Comparator;
import java.util.concurrent.*;


/**
 * The {@link Server} implementation.
 *
 * @author Ronen Zolicha
 */
public class MyServer implements Server {


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

    private ExecutorService priorityJobPoolExecutor;
    private ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private PriorityBlockingQueue<Job> priorityQueue;


    /**
     * Creates new {@link MyServer} with the specified port.
     */
    public MyServer(int port, int poolSize, int queueSize) {
        this.port = port;
        setExecutor(poolSize);
        priorityQueueInitialization(queueSize);
    }


    @Override
    public void start(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.stop = false;

        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void stop() {
        try {
            priorityJobPoolExecutor.awaitTermination(5000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.clientHandler = null;
        this.stop = true;
    }


    /**
     * Run & listen on the server port.
     */
    private void runServer() throws Exception {
        ServerSocket server = new ServerSocket(port);
        server.setSoTimeout(1000);

        System.out.println("Waiting for clients on port: " + port);
        while (!stop) {
            try {
                Socket socket = server.accept();
                priorityJobScheduler.execute(() -> {
                    try {
                        Job newJob = new Job(socket, clientHandler);
                        priorityQueue.put(newJob);
                        Job jobToExecute = priorityQueue.take();
                        if (jobToExecute != null){
                            System.out.println(priorityJobPoolExecutor);
                            priorityJobPoolExecutor.execute(jobToExecute);
                        }
    //					InputStream inFromClient = socket.getInputStream();
    //					OutputStream outToClient = socket.getOutputStream();
    //					clientHandler.handleClient(inFromClient, outToClient);
    //					inFromClient.close();
    //					outToClient.close();
    //					socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


            } catch (SocketTimeoutException e) {
//				e.printStackTrace();
            }
        }

        server.close();
        System.out.println("Server closed!");
    }

    private void setExecutor(int poolSize) {
        final int minPoolSize = 1;

        if (poolSize < minPoolSize) {
            throw new IllegalArgumentException("Pool size value out of range: " + poolSize);
        } else {
            priorityJobPoolExecutor = Executors.newFixedThreadPool(poolSize);
        }
    }

    private void priorityQueueInitialization(int queueSize) {
        final int emptyQueueSize = 0;
        if (queueSize <= emptyQueueSize) {
            throw new IllegalArgumentException("Queue size must be greater than 0");
        }
        priorityQueue = new PriorityBlockingQueue<Job>(queueSize, Comparator.comparing(Job::getPriority));
    }


}