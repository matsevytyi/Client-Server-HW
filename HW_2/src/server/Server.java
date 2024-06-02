package server;

// Importing required libraries
import org.example.CustomKey;

import java.io.*;
import java.net.*;


public class Server {


    public static void main(String[] args) {


        try {
            Reciever.receive();
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            // Display the exception on the console
            System.out.println(e);
        }
    }
}
