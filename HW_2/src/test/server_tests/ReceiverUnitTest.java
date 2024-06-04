package server_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import src.client.Client;
import src.packet_handling.CustomKey;
import src.server.MTDecoder;
import src.server.Reciever;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ReceiverUnitTest {

    @Test
    public void testReceive() throws IOException, InterruptedException {
        // Mock the MTDecoder static method
        try (MockedStatic<MTDecoder> mockedDecoder = Mockito.mockStatic(MTDecoder.class);
             MockedStatic<Client> mockedClient = Mockito.mockStatic(Client.class)) {

            CustomKey key = new CustomKey();
            mockedClient.when(Client::getKey).thenReturn(key);

            // Start a thread to run the Reciever.receive() method
            Thread serverThread = new Thread(() -> {
                try {
                    Reciever.receive(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            int prev_count = Thread.activeCount();

            // Start a client thread to connect to the server and send a message
            Thread clientThread = new Thread(() -> {
                try {
                    Thread.sleep(1000); // Give the server a moment to start
                    try (Socket clientSocket = new Socket("127.0.0.1", 6667);
                         DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

                        dos.writeUTF("testMessage");
                        dos.flush();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            serverThread.start();

            clientThread.start();

            // Verify that MTDecoder.decode() was called since it invokes a new thread
            Assertions.assertTrue(prev_count + 1 < Thread.activeCount());
        }
    }
}
