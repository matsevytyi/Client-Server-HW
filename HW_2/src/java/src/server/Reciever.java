package src.server;

import src.client.Client;
import src.packet_handling.CustomKey;
import src.server.MTDecoder;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface Reciever {
    public static void receive(CustomKey key) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6667);

        // Thread to monitor for 'K' key press and close the server socket
        Thread keyPressMonitor = new Thread(() -> {
            try {
                while (true) {
                    int keyIn = System.in.read();
                    if (keyIn == 'K' || keyIn == 'k') {
                        serverSocket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        keyPressMonitor.start();

        while (!serverSocket.isClosed()) {
            try {
                // Establishing a connection
                Socket clientSocket = serverSocket.accept();

                // Handle each connection in a separate thread
                new Thread(() -> handleClient(clientSocket, key)).start();

            } catch (IOException e) {
                if (serverSocket.isClosed()) {
                    break; // Exit loop if the server socket is closed
                }
                e.printStackTrace();
            }
        }
    }

    static void handleClient(Socket clientSocket, CustomKey key) {
        try {

            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            // Parse socket input
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            String recipientIP = clientSocket.getInetAddress().getHostAddress();

            System.out.println("Starting decoding...");

            MTDecoder.decode(clientSocket.getInputStream().readAllBytes(), key, recipientIP);

            System.out.println("Decoding complete.");

            clientSocket.close(); // Close the client socket after processing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
