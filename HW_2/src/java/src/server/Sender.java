package src.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class Sender {

    //ip is passed
    public static void send(byte[] input, String message, Socket clientSocket) {
        System.out.println("Sending: " + message + " to " + clientSocket.getInetAddress().getHostAddress());
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(Base64.getEncoder().encodeToString(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendError(Socket clientSocket) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("Error");
            System.out.println("Error sent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
