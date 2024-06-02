package server;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketDecoder;

class DecoderThread extends Thread {

    private byte[] input;
    private CustomKey key;

    private String recipientIP;


    public void run() {
        super.run();
        Packet decodedPacket = PacketDecoder.decodePacket(this.input, this.key.getSecretKey());
        MTProcessor.process(decodedPacket, key, recipientIP);
    }

    public DecoderThread(byte[] input, CustomKey key, String recipientIP) {
        this.input = input;
        this.key = key;
        this.recipientIP = recipientIP;
        this.start();
    }
}

public class MTDecoder {
    int currentId = 0;
    public static void decode(byte[] input, CustomKey key, String recipientIP) {
        new DecoderThread(input, key, recipientIP);
    }
}
