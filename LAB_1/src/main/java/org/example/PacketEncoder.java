package org.example;

import javax.crypto.*;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class PacketEncoder {

    public static byte[] encodePacket(Packet packet, SecretKey publicKey) {
        byte bSrc = packet.getbSrc();
        long bPktId = packet.getbPktId();
        byte[] message = packet.getMessage().toBytes();
        // encrypt message
        message = encode(message, publicKey);
        Packet newPacket = new Packet(bSrc, bPktId, message);
        return newPacket.toBytes();
    }

    private static byte[] encode(byte[] data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            data = cipher.doFinal(data);
            return Base64.getEncoder().encode(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }


}
