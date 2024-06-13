package org.example;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;

public class PacketDecoder {


    public static Packet decodePacket(byte[] input, SecretKey privateKey) {
        if(input[0] != 0x13){
            throw new IllegalArgumentException("Illegal packet signature");
        }

        if(input.length < 18) throw new IllegalArgumentException("Packet is broken: header is lost");

        byte[] wLen = Arrays.copyOfRange(input, 10, 14);
        int MessageLength = (int) ((wLen[0] << 24) | (wLen[1] << 16) | (wLen[2] << 8) | wLen[3]);

        //check length
        if(input.length < 18 + MessageLength) throw new IllegalArgumentException("Packet is broken: message is lost");

        //check crc16 and crc16e
        byte[] init_crc16 = Arrays.copyOfRange(input, 14, 16);
        byte[] actual_crc16 = Packet.calculateCrc16(Arrays.copyOfRange(input, 0, 14));

        //System.out.println(init_crc16[0] + " " + init_crc16[1]);
        //System.out.println(actual_crc16[0] + " " + actual_crc16[1]);

        byte[] init_crc16e = Arrays.copyOfRange(input, 16 + MessageLength, 16 + MessageLength + 2);
        byte[] actual_crc16e = Packet.calculateCrc16(Arrays.copyOfRange(input, 16, MessageLength + 16));

        //System.out.println(init_crc16e[0] + " " + init_crc16e[1]);
        //System.out.println(actual_crc16e[0] + " " + actual_crc16e[1]);

        byte[] decryptedMessage = decrypt(Arrays.copyOfRange(input, 16, 16 + MessageLength), privateKey);

        if(!Arrays.equals(init_crc16, actual_crc16) || !Arrays.equals(init_crc16e, actual_crc16e))
            throw new IllegalArgumentException("Packet is broken: Crc16 validation error");

        return new Packet(input[1], Arrays.copyOfRange(input, 2, 10), decryptedMessage);
    }

    public static Message decodePacketMessage(byte[] input, SecretKey privateKey) {
        return decodePacket(input, privateKey).getMessage();
    }


    //decrypt function

    private static byte[] decrypt(byte[] data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            data = Base64.getDecoder().decode(data);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

}
