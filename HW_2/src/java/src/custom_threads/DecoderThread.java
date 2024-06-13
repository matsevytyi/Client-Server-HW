package src.custom_threads;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketDecoder;
import src.server.MTProcessor;

import java.net.Socket;

public class DecoderThread extends Thread {

    private byte[] input;
    private CustomKey key;

    private String recipientIP;

    Socket clientSocket;


    public void run() {
        super.run();
        Packet decodedPacket = PacketDecoder.decodePacket(this.input, this.key.getSecretKey());
        System.out.println("Received: " + decodedPacket.getMessage().getMessage() + " from " + recipientIP);
        MTProcessor.process(decodedPacket, key, recipientIP, clientSocket);
    }

    public DecoderThread(byte[] input, CustomKey key, String recipientIP, Socket clientSocket) {
        this.input = input;
        this.key = key;
        this.recipientIP = recipientIP;
        this.clientSocket = clientSocket;
        this.start();
    }
}
