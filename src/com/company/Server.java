package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int port;
    public static ServerSocket S;

    public static void Initiate() throws IOException {
        port = 2000;
        S = new ServerSocket(port);
    }
    public static void main(String[] args) throws IOException {
        Initiate();
        int i = 0;
        while (true){
            Socket ss = S.accept();
            new Thread(new MyThread(ss)).start();
            System.out.println("j");
        }


    }

}
