package multi_thread_integration_tests;

import org.example.CustomKey;
import src.client.Client;
import src.server.Reciever;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MultiThreadIntegrationTest {
    @Test
    public void IntegrationMultithreadTest(){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        int numberOfClients = 10; // Number of clients to launch

        CustomKey sharedKey = new CustomKey();


        // Launch a server
        Thread serverThread = new Thread(() -> {
            try {
                Reciever.receive(sharedKey);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();


        // Launch multiple clients
        for (int i = 1; i <= numberOfClients; i++) {

            Thread clientThread = new Thread(() -> {
                Client.launchClient(sharedKey);
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            clientThread.start();
        }

        String output = outputStream.toString();

        Assertions.assertTrue(output.contains("Client connected: 127.0.0.1"));
        Assertions.assertTrue(output.contains("Starting decoding..."));
        Assertions.assertTrue(output.contains("Decoding complete"));
        Assertions.assertTrue(output.contains("OK to 127.0.0.1"));

        // Reset System.out
        System.setOut(System.err);
        System.out.println(output);

    }
}
