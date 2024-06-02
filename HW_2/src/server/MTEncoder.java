package server;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketEncoder;

class EncoderThread extends Thread {
    Packet packet;
    CustomKey key;

    String message;

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
public class MTEncoder {

    public static void encode(Packet packet, CustomKey key, String recipientIP, String message) {
        new EncoderThread(packet, key, recipientIP, message);
    }
}
