package server;

import client.Client;
import org.example.CustomKey;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface Reciever {
    public static void receive() throws IOException {
        ServerSocket ss = new ServerSocket(6667);

        // Establishing a connection
        Socket soc = ss.accept();

        //parse socket input
        DataInputStream dis = new DataInputStream(soc.getInputStream());

        String recipientIP = soc.getInetAddress().getHostAddress();

        CustomKey key = Client.getKey();

        MTDecoder.decode(soc.getInputStream().readAllBytes(), new CustomKey(), recipientIP);


        ss.close();
    }
}
