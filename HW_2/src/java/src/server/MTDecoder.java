package src.server;

import src.packet_handling.CustomKey;
import src.custom_threads.DecoderThread;

public class MTDecoder {
    int currentId = 0;
    public static void decode(byte[] input, CustomKey key, String recipientIP) {
        new DecoderThread(input, key, recipientIP);
    }
}
