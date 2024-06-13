package org.example;


public class Main {

    public static void main(String[] args) {

        CustomKey key = new CustomKey();
        System.out.println(key.getSecretKey().getAlgorithm());
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "Command").toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, key.getSecretKey());
        for (byte b : encoded) {
            System.out.print((char) b + " ");
        }

        System.out.println("\n-------------------\n");

        byte[] bytes = packet.toBytes();
        Packet decoded = PacketDecoder.decodePacket(encoded, key.getSecretKey());
       for (byte b : packet.toBytes()) {
           System.out.print( (char) b + " ");
       }

       System.out.println("\n-------------------\n");
       Message message = decoded.getMessage();
       byte[] messageBytes = message.toBytes();
       for (byte b : messageBytes) {
           System.out.print( (char) b + " ");
       }

    }
}