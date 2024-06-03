package src.packet_handling;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PacketEncoder {

    public static byte[] encodePacket(Packet packet, SecretKey publicKey) {
        byte bSrc = packet.getbSrc();
        byte[] bPktId = packet.getbPktId();
        byte[] message = packet.getMessage().toBytes();
        // encrypt message
        message = encode(message, publicKey);
        Packet newPacket = new Packet(bSrc, bPktId, message);
        return newPacket.toBytes();
    }

    private static byte[] encode(byte[] data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }


}
