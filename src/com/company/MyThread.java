package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class MyThread implements Runnable {

    public static Socket ss;
    public static BufferedReader br;
    public static PrintWriter bw;

    public MyThread(Socket ss) throws IOException {
        this.ss = ss;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            readRequest();
            ss.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readRequest() throws IOException {
        String riga[] = br.readLine().split(" ");
        //Legge tutta la richiesta
        while (!(br.readLine()).equals("")) ;

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
        System.out.println(directory);
        if (directory.equals("/"))
            directory = "/index.html";
        directory = "www" + directory;
        System.out.println(directory);
        if (new File(directory).exists()) {
            //System.out.println(directory);
            if (directory.split("\\.")[1].equals("html"))
                sendpage(directory);
            else
                sendObj(directory);

        } else {
            fileNotFoundError();
        }


    }

    public static void sendObj(String directory) throws IOException {
        byte[] data = Files.readAllBytes(new File(directory).toPath());
        bw.println("HTTP/1.1 200 OK");
        bw.println("Content length: 0" + data.length);
        bw.println("Content Type: image/" + directory.split("\\.")[1]);
        bw.println("Keep-Alive: timeout=5, max=100");
        bw.println("Connection: Keep-Alive");
        bw.println();

        OutputStream os = ss.getOutputStream();
        os.write(data);

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
        bw.println("Keep-Alive: timeout=5, max=100");
        bw.println("Connection: Keep-Alive");
        bw.println();
        bw.println(content);
    }

    public static void fileNotFoundError() throws IOException {
        String content = "";
        BufferedReader fr = new BufferedReader(new FileReader("www/error.html"));
        String riga;
        while ((riga = fr.readLine()) != null)
            content += riga;

        bw.println("HTTP/1.1 404 Not Found");
        bw.println("Content length: " + content.getBytes().length);
        bw.println("Content Type: text/html");
        bw.println("Keep-Alive: timeout=5, max=100");
        bw.println("Connection: Keep-Alive");
        bw.println();
        bw.println(content);
    }

}
