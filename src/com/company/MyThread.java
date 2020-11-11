package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class MyThread implements Runnable {

    public static Socket ss;
    public static BufferedReader br;
    public static PrintWriter bw;

    public MyThread(Socket ss) {
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
            sendObj("200 OK", directory);
        } else {
            fileNotFoundError();
        }
    }

    public static void sendObj(String resp, String directory) throws IOException {
        byte[] data = Files.readAllBytes(new File(directory).toPath());
        String contenttype = "";
        String extension = "";
        try {
            extension = directory.split("\\.")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        switch (extension) {
            case "html":
                contenttype = "text/html";
                break;
            default:
                contenttype = "image/" + extension;
                break;
        }

        byte[] header = ("HTTP/1.1 " + resp + "\n" +
                "Keep-Alive: timeout=50, max=100\n" +
                "Connection: Keep-Alive\n" + "Content-Type: " + contenttype + "\n" +
                "Content length: " + data.length + "\n" +
                "\n").getBytes();

        byte content[] = new byte[header.length + data.length];
        System.arraycopy(header, 0, content, 0, header.length);
        System.arraycopy(data, 0, content, header.length, data.length);
        ss.getOutputStream().write(content);
    }


    public static void fileNotFoundError() throws IOException {
        sendObj("404 Not Found", "www/error.html");
    }

}
