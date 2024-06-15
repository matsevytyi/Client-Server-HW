package org.example;

import src.server.MTDecoder;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class StoreClientUDP {
    private String serverAddress;
    private int serverPort;

    long currentPacketID = 0;

    public StoreClientUDP(String serverAddress, int serverPort) {
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
                String response = reader.readLine();
                if(response.equals("DONE")){
                    System.out.println("Handshake successful");
                    break;
                }
            }

            byte[] bytes = PacketEncoder.encodePacket(new Packet((byte) 1, currentPacketID, new Message(1, 1, "Hello").toBytes()), key.getSecretKey());
            System.out.println("Sent: " + Base64.getEncoder().encodeToString(bytes));
            writer.println(Base64.getEncoder().encodeToString(bytes));
            System.out.println("Waiting for response...");

            while (true) {
                String response = reader.readLine();
                while (response == "Error") {
                    response = reader.readLine();
                    byte[] newBytes = Base64.getDecoder().decode(response);
                    Packet packet = PacketDecoder.decodePacket(newBytes, key.getSecretKey());
                    System.out.println("Received: " + packet.getMessage() + " from " + socket.getInetAddress().getHostAddress());
                    if (packet.getbPktId() != currentPacketID + 1) {
                        writer.println("Error");
                    } else {
                        packet.setbPktId(++currentPacketID);
                        writer.println(Base64.getEncoder().encodeToString(newBytes));
                    }
                }
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
        StoreClientUDP client = new StoreClientUDP("127.0.0.1", 1234);
        try {
            client.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
