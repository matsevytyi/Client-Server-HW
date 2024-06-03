package src.custom_threads;

import src.packet_handling.CustomKey;
import src.packet_handling.Message;
import src.packet_handling.Packet;
import src.server.MTEncoder;

public class ProcessorThread extends Thread {

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
