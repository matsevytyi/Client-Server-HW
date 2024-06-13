package org.example;

import src.server.MTDecoder;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class StoreClientTCP {
    private String serverAddress;
    private int serverPort;

    private int packetsSent = 0;
    private int packetsReceived = 0;

    public StoreClientTCP(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() throws InterruptedException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            //send key as a handshake
            CustomKey key = new CustomKey();

            while(true){
                writer.println(key.getEncodedKey());
                packetsSent++;
                String response = reader.readLine();
                packetsReceived++;
                if(response.equals("DONE")){
                    System.out.println("Handshake successful");
                    break;
                }
            }

            System.out.println("Enter message: ");

            byte[] bytes = PacketEncoder.encodePacket(new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, new Message(1, 1, "Hello").toBytes()), key.getSecretKey());
            System.out.println("Sent: " + Base64.getEncoder().encodeToString(bytes));
            writer.println(Base64.getEncoder().encodeToString(bytes));
            System.out.println("Waiting for response...");

            while (true) {
                String response = reader.readLine();
                packetsReceived++;
                byte[] newBytes = Base64.getDecoder().decode(response);
                System.out.println("Received: " + PacketDecoder.decodePacketMessage(newBytes, key.getSecretKey()).getMessage() + " from " + socket.getInetAddress().getHostAddress());
                writer.println(Base64.getEncoder().encodeToString(newBytes));
                packetsSent++;
            }
        } catch (IOException e) {
            // try to connect again to the server (handling ConnectException)
            System.out.println("Server out, reconnecting in 3 seconds...");
            Thread.sleep(3000);
            start();
        } catch (NullPointerException e) {
            // try to connect again to the server
            System.out.println("Server out, reconnecting in 3 seconds...");
            Thread.sleep(3000);
            start();
        }
    }

    public static void main(String[] args) {
        StoreClientTCP client = new StoreClientTCP("127.0.0.1", 12345);
        try {
            client.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
