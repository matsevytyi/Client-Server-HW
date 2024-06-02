package server;

import org.example.PacketDecoder;

public class Sender {

    //ip is passed
    public static void send(byte[] input, String recipientIP, String message) {
        System.out.println("Sending: " + message + " to " + recipientIP);
    }
}
