package src.server;

import org.example.CustomKey;
import org.example.Packet;
import src.custom_threads.EncoderThread;

import java.net.Socket;

public class MTEncoder {

    public static void encode(Packet packet, CustomKey key, String message, Socket clientSocket) {
        new EncoderThread(packet, key, message, clientSocket);
    }
}
