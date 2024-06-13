package src.custom_threads;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketDecoder;
import src.server.MTProcessor;

public class DecoderThread extends Thread {

    private byte[] input;
    private CustomKey key;

    private String recipientIP;


    public void run() {
        super.run();
        Packet decodedPacket = PacketDecoder.decodePacket(this.input, this.key.getSecretKey());
        System.out.println("Received: " + decodedPacket.getMessage().getMessage() + " from " + recipientIP);
        MTProcessor.process(decodedPacket, key, recipientIP);
    }

    public DecoderThread(byte[] input, CustomKey key, String recipientIP) {
        this.input = input;
        this.key = key;
        this.recipientIP = recipientIP;
        this.start();
    }
}
