package src.custom_threads;

import src.packet_handling.CustomKey;
import src.packet_handling.Packet;
import src.packet_handling.PacketEncoder;
import src.server.Sender;

public class EncoderThread extends Thread {
    private Packet packet;
    private CustomKey key;

    private String message;

    private String recipientIP;




    public void run() {
        super.run();
        byte[] encoded = PacketEncoder.encodePacket(packet, key.getSecretKey());
        //logic
        Sender.send(encoded, recipientIP, message);
    }

    public EncoderThread(Packet packet, CustomKey key, String recipientIP, String message) {
        this.message = message;
        this.packet = packet;
        this.key = key;
        this.recipientIP = recipientIP;
        this.start();
    }
}
