package src.custom_threads;

import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;
import src.server.MTEncoder;

import java.net.Socket;

public class ProcessorThread extends Thread {

    Packet input;
    CustomKey key;

    Socket clientSocket;

    private String recipientIP;

    public void run() {
        super.run();
        int command = input.getMessage().getcType();
        //TODO implement appropriate logic when needed
        String msg = "OK";
        Message message = new Message(1, 1, msg);
        //System.out.println(message.toBytes().length);
        MTEncoder.encode(new Packet(input.getbSrc(), input.getbPktId(), message.toBytes()), key, recipientIP, msg, clientSocket);
    }

    public ProcessorThread(Packet packet, CustomKey key, String recipientIP, Socket clientSocket) {
        this.input = packet;
        this.key = key;
        this.recipientIP = recipientIP;
        this.clientSocket = clientSocket;
        this.start();
    }
}
