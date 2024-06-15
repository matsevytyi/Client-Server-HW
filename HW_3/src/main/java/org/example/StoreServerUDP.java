package org.example;

import src.server.MTDecoder;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.*;

public class StoreServerUDP {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public StoreServerUDP(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newCachedThreadPool();
        System.out.println("Server TCP started on port " + port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        long currentPacketID = 1;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 Scanner scanner = new Scanner(System.in)) {

                String destinationIP = clientSocket.getInetAddress().getHostAddress();

                CustomKey key = new CustomKey();

                while(true){
                    String response = reader.readLine();
                    if(!response.isBlank()){
                        key = new CustomKey(response, true);
                        writer.println("DONE");
                        System.out.println("Handshake successful");
                        break;
                    }

                }

                while (true) {
                    String response = null;
                    do {
                        if (response == "Error") currentPacketID--;
                        response = reader.readLine();
                        byte[] bytes = Base64.getDecoder().decode(response);
                        //message is send in the end of the process invoked by MTDecoder (MTD -> MTP -> MTE -> Sender)
                        MTDecoder.decode(bytes, key, currentPacketID, clientSocket);
                        currentPacketID++;
                        System.out.println("Received: " + response);
                    } while (response == "Error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        StoreServerUDP server = new StoreServerUDP(1234);
        server.start();
    }
}
