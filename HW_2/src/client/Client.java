package client;


// Importing required libraries
import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;
import org.example.PacketEncoder;

import java.io.*;
import java.net.*;

// Main class
public class Client {

    static CustomKey key;

    public static CustomKey getKey() {
        return key;
    }


    // Main driver method
    public static void main(String[] args)
    {

        // Try block to check if exception occurs
        try {

            Socket soc = new Socket("127.0.0.1", 6667);

            DataOutputStream d = new DataOutputStream(
                    soc.getOutputStream());

            // Making key available to Server.java. TODO On future to change it to PGP enctyption
            Client.key = new CustomKey();

            byte[] encoded = MessageGenerator.generateMsg(key);


            d.write(encoded);

            // Flushing out internal buffers,
            // optimizing for better performance
            d.flush();

            // Closing the connections

            d.close();
            soc.close();
        }

        // Catch block to handle exceptions
        catch (Exception e) {

            // Print the exception on the console
            System.out.println(e);
        }
    }
}

