package src.server;

import org.example.CustomKey;
import org.example.Packet;
import src.custom_threads.ProcessorThread;

import java.net.Socket;

public class MTProcessor {

    public static void process(Packet packet, CustomKey key, long pktId, Socket clientSocket) {
        new ProcessorThread(packet, key, pktId, clientSocket);
    }
}
