package com.zoliron.server;

import com.zoliron.client.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Job implements Runnable{

    private Socket socket;
    private ClientHandler clientHandler;

    public Job(Socket socket, ClientHandler clientHandler) {
        this.socket = socket;
        this.clientHandler = clientHandler;
    }


    @Override
    public void run() {
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
    }

    public static <U extends Comparable<? super U>, T> int getPriority(T t) {
        return 10;
    }
}
