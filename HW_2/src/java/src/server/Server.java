package src.server;


import src.packet_handling.CustomKey;

public class Server {


    public static void main(String[] args) {

        CustomKey key = new CustomKey();

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
