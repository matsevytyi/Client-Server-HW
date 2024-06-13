package src.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class Sender {

    //ip is passed
    public static void send(byte[] input, String recipientIP, String message, Socket clientSocket) {
        System.out.println("Sending: " + message + " to " + recipientIP);
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(Base64.getEncoder().encodeToString(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
