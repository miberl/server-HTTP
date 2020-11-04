package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static Socket ss;
    public static BufferedReader br;
    public static PrintWriter bw;

    public static void main(String[] args) throws IOException, IOException {

        int PORTA = 2000;  // porta di ascolto
        ServerSocket S;
        S = new ServerSocket(PORTA);

        while (true) {
            ss = S.accept();
            br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
            readRequest();
            ss.close();
        }

    }


    public static void readRequest() throws IOException {
        String riga[] = br.readLine().split(" ");
        if (riga.length == 3) {
            switch (riga[0]) {
                case "GET":
                    get(riga[1]);
                    break;
                default:
                    break;
            }
        }

    }

    public static void get(String directory) throws IOException {
        if (directory.equals("/"))
            directory = "/index.html";
        directory = "www" + directory;
        System.out.println(directory);
        sendpage(directory);
    }


    public static void sendpage(String directory) throws IOException {
        String content = "";
        BufferedReader fr = new BufferedReader(new FileReader(directory));
        String riga;
        while ((riga = fr.readLine()) != null)
            content += riga;

        bw.println("HTTP/1.1 200 OK");
        bw.println("Content length: " + content.getBytes().length);
        bw.println("Content Type: text/html");
        bw.println();
        bw.println(content);
    }

}
