package src.client;


// Importing required libraries
import src.packet_handling.CustomKey;

import java.io.*;
import java.net.*;

// Main class
public class Client {

    static CustomKey key;

    public static CustomKey getKey() {
        return key;
    }

    public static void setKey(CustomKey key) {
        Client.key = key;
    }


    // Main driver method
    public static void launchClient(CustomKey key)
    {

        // Try block to check if exception occurs
        try {

            Socket soc = new Socket("127.0.0.1", 6667);

            DataOutputStream d = new DataOutputStream(
                    soc.getOutputStream());

            //TODO On future to change it to PGP enctyption

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

    public static void main(String[] args) {
        launchClient(new CustomKey());
    }
}

