package src.server;

import org.example.CustomKey;
import src.custom_threads.DecoderThread;

import java.net.Socket;

public class MTDecoder {
    public static void decode(byte[] input, CustomKey key, long pktId, Socket clientSocket) {
        new DecoderThread(input, key, pktId, clientSocket);
    }
}
