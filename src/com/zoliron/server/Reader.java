package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;



public class Reader implements Runnable{



	private final Socket socket;
	private final ClientHandler clientHandler;
	private final Executor executor;



	Reader(Socket socket, ClientHandler clientHandler, ExecutorService executor){
		this.socket = socket;
		this.clientHandler = clientHandler;
		this.executor = executor;
	}



	@Override
	public void run(){
		try{
			InputStream inFromClient = socket.getInputStream();
			byte[] input = readToEnd(inFromClient);

			executor.execute(new Job(socket, clientHandler, input));
		} catch (Exception e){
			e.printStackTrace();
		}
	}



	/**
	 * Read the problem input until "done".
	 */
	private byte[] readToEnd(InputStream inFromClient) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(baos));

		BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
		String line;
		while ((line = br.readLine()) != null){
			bw.write(line);

			if ("done".equals(line))
				break;

			bw.newLine();
		}

		bw.flush();

		return baos.toByteArray();
	}



}
