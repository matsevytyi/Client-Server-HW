package src.custom_threads;

import src.packet_handling.CustomKey;
import src.packet_handling.Packet;
import src.packet_handling.PacketDecoder;
import src.server.MTProcessor;

public class DecoderThread extends Thread {

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
