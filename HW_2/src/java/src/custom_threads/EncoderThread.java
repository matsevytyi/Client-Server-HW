package src.custom_threads;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketEncoder;
import src.server.Sender;

import java.net.Socket;

public class EncoderThread extends Thread {
    private Packet packet;
    private CustomKey key;

    private String message;

    Socket clientSocket;




    public void run() {
        super.run();
        byte[] encoded = PacketEncoder.encodePacket(packet, key.getSecretKey());
        //logic
        Sender.send(encoded, message, clientSocket);
    }

    public EncoderThread(Packet packet, CustomKey key, String message, Socket clientSocket) {
        this.message = message;
        this.packet = packet;
        this.key = key;
        this.clientSocket = clientSocket;
        this.start();
    }
}
