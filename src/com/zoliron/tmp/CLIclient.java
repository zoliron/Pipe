package com.zoliron.tmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;



public class CLIclient{



	private void readInputsAndSend(BufferedReader in, PrintWriter out, String exitStr){
		try{
			String answer;
			while (!(answer = in.readLine()).equals(exitStr)){
				out.println(answer);
				out.flush();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}



	public void start(String ip, int port){
		try{
			Socket newServer = new Socket(ip, port);
			System.out.println("connected to server");

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(newServer.getInputStream()));

			PrintWriter outToServer = new PrintWriter(newServer.getOutputStream());
			PrintWriter outToScreen = new PrintWriter(System.out);

			readInputsAndSend(userInput, outToServer, "exit");
			readInputsAndSend(serverInput, outToScreen, "bye");

			userInput.close();
			serverInput.close();
			outToServer.close();
			outToScreen.close();
			newServer.close();
		} catch (UnknownHostException e){
			System.out.println("Unknown Host");
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}



	public static void main(String[] args) throws Exception{
		CLIclient cli = new CLIclient();
		cli.start("127.0.0.1", 6400);
	}

}

