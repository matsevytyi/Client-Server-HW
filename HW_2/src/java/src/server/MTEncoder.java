package src.server;

import src.packet_handling.CustomKey;
import src.packet_handling.Packet;
import src.custom_threads.EncoderThread;

public class MTEncoder {

    public static void encode(Packet packet, CustomKey key, String recipientIP, String message) {
        new EncoderThread(packet, key, recipientIP, message);
    }
}
