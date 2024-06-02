package server;

import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;

class ProcessorThread extends Thread {

    Packet input;
    CustomKey key;

    private String recipientIP;

    public void run() {
        super.run();
        String command = input.getMessage().getcType();
        //TODO implement appropriate logic when needed
        String msg = "      OK";
        Message message = new Message("aswr", "1234", msg.getBytes());
        //System.out.println(message.toBytes().length);
        MTEncoder.encode(new Packet(input.getbSrc(), input.getbPktId(), message.toBytes()), key, recipientIP, msg);
    }

    public ProcessorThread(Packet packet, CustomKey key, String recipientIP) {
        this.input = packet;
        this.key = key;
        this.recipientIP = recipientIP;
        this.start();
    }
}

public class MTProcessor {

    public static void process(Packet packet, CustomKey key, String recipientIP) {
        new ProcessorThread(packet, key, recipientIP);
    }
}
