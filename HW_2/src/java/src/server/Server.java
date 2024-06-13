package src.server;


import org.example.CustomKey;
import src.client.Client;

public class Server {

    static CustomKey key;

    public static CustomKey getKey() {
        return key;
    }

    public static void setKey(CustomKey key) {
        Server.key = key;
    }


    public static void main(String[] args) {

        key = new CustomKey();
        key.saveToFile("LAB_1/src/main/resources/key.txt");

        try {
            Reciever.receive(key);
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            // Display the exception on the console
            System.out.println(e);
        }
    }
}
