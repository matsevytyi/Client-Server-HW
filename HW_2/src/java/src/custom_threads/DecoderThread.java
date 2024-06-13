package src.custom_threads;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketDecoder;
import src.server.MTProcessor;

import java.net.Socket;

public class DecoderThread extends Thread {

    private byte[] input;
    private CustomKey key;

    private long pktId;

    Socket clientSocket;


    public void run() {
        super.run();
        Packet decodedPacket = PacketDecoder.decodePacket(this.input, this.key.getSecretKey());
        System.out.println("Received: " + decodedPacket.getMessage().getMessage() + " from " + clientSocket.getInetAddress().getHostAddress());
        MTProcessor.process(decodedPacket, key, pktId, clientSocket);
    }

    public DecoderThread(byte[] input, CustomKey key, long pktId, Socket clientSocket) {
        this.input = input;
        this.key = key;
        this.pktId = pktId;
        this.clientSocket = clientSocket;
        this.start();
    }
}
