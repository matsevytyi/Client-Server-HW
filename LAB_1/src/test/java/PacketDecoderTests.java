import org.example.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class PacketDecoderTests {
    @Test
    public void testEncodeDecodePacket() throws Exception {
        // Generate a secret key
        SecretKey secretKey = new CustomKey().getSecretKey();

        // Create a packet
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "Command").toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);


        // Decode the packet
        Packet decodedPacket = PacketDecoder.decodePacket(encoded, secretKey);

        // Assert that the decoded packet is equal to the original packet
        assertArrayEquals(packet.toBytes(), decodedPacket.toBytes());
    }

    @Test
    public void testExtractedMessage(){
        byte[] testMessage = new Message(1, 1, "Command").toBytes();

        SecretKey secretKey = new CustomKey().getSecretKey();

        // Create a packet
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, testMessage);
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);

        Packet decodedPacket = PacketDecoder.decodePacket(encoded, secretKey);
        assertArrayEquals(testMessage, decodedPacket.getMessage().toBytes());
    }

    @Test
    public void testIllegalFirstByte(){
        SecretKey secretKey = new CustomKey().getSecretKey();
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "Command").toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);
        encoded[0] += 1;

        Assert.assertThrows(IllegalArgumentException.class, () -> PacketDecoder.decodePacket(encoded, secretKey));
    }

    @Test
    public void testCrc16Error(){
        SecretKey secretKey = new CustomKey().getSecretKey();
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "Command").toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);

        encoded[14] += 1;

        Assert.assertThrows(IllegalArgumentException.class, () -> PacketDecoder.decodePacket(encoded, secretKey));
    }

    @Test
    public void testCrc16eError(){
        SecretKey secretKey = new CustomKey().getSecretKey();
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "Command").toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);

        encoded[32] += 1;

        Assert.assertThrows(IllegalArgumentException.class, () -> PacketDecoder.decodePacket(encoded, secretKey));
    }

}
