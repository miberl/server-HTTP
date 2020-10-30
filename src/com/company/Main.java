package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static Socket ss;
    public static PrintWriter bw;
    public static String pagina;

    public static void main(String[] args) throws IOException, IOException {

        int PORTA = 2000;  // porta di ascolto
        ServerSocket S;
        pagina = "<html><head><title>FUNZIONA</title></head><body><h1>Ciao a tutti belli e brutti</h1></body></html>";
        S = new ServerSocket(PORTA);
        while (true){
            ss = S.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
            String riga = null;
            while (!(riga = br.readLine()).equals("")) ;
            sendpage();
            ss.close();
        }

    }

    public static void sendpage() throws IOException {
        bw.println("HTTP/1.1 200 OK");
        bw.println("Content length: " + pagina.getBytes().length);
        bw.println();
        bw.println(pagina);
    }
}