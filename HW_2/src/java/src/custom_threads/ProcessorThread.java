package src.custom_threads;

import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;
import src.server.MTEncoder;
import src.server.Sender;

import java.net.Socket;

public class ProcessorThread extends Thread {

    Packet inputPacket;
    CustomKey key;

    Socket clientSocket;

    private long pktId;

    public void run() {
        super.run();
        int command = inputPacket.getMessage().getcType();
        //TODO implement appropriate logic when needed
        if (inputPacket.getbPktId() != pktId)
            {
                System.out.println("Packet Lost, trying again");
                System.out.println("Actual Packet ID: " + pktId + " Received Packet ID: " + inputPacket.getbPktId());
                Sender.sendError(clientSocket);
            }
        else {
                String msg = "OK";
                Message message = new Message(1, 1, msg);
                //System.out.println(message.toBytes().length);

                long newPktId = inputPacket.getbPktId() + 1;

                MTEncoder.encode(new Packet(inputPacket.getbSrc(), newPktId, message.toBytes()), key, msg, clientSocket);

            }
        }

    public ProcessorThread(Packet packet, CustomKey key, long pktId, Socket clientSocket) {
        this.inputPacket = packet;
        this.key = key;
        this.pktId = pktId;
        this.clientSocket = clientSocket;
        this.start();
    }
}
