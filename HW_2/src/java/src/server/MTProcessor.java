package src.server;

import src.packet_handling.CustomKey;
import src.packet_handling.Packet;
import src.custom_threads.ProcessorThread;

public class MTProcessor {

    public static void process(Packet packet, CustomKey key, String recipientIP) {
        new ProcessorThread(packet, key, recipientIP);
    }
}
