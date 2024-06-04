package src.server;

import src.packet_handling.PacketDecoder;

public class Sender {

    //ip is passed
    public static void send(byte[] input, String recipientIP, String message) {
        System.out.println("Sending: " + message + " to " + recipientIP);
    }
}
