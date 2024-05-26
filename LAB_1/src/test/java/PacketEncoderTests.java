import org.example.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PacketEncoderTests {

    @Test
    public void testEncodePacket() throws Exception {
        // Generate a secret key
        SecretKey secretKey = new CustomKey().getSecretKey();

        // Create a packet
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message("ping", "1234", new byte[]{1, 2, 3, 4, 5, 6, 7, 8}).toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);

        Assert.assertEquals(packet.toBytes().length, encoded.length);

        Assert.assertEquals(encoded[2], packet.getbSrc());
    }

    @Test
    public void testEncryption() throws Exception {
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message("ping", "1234", new byte[]{1, 2, 3, 4, 5, 6, 7, 8}).toBytes());
        SecretKey secretKey = new CustomKey().getSecretKey();
        byte[] encoded = PacketEncoder.encodePacket(packet, secretKey);

        Assert.assertNotEquals(encoded, packet.toBytes());
    }

    @Test
    public void testCrc16() throws Exception {
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message("ping", "1234", new byte[]{1, 2, 3, 4, 5, 6, 7, 8}).toBytes());
        byte[] encoded = PacketEncoder.encodePacket(packet, new CustomKey().getSecretKey());
        byte[] init_crc16 = Arrays.copyOfRange(encoded, 14, 16);
        byte[] actual_crc16 = Packet.calculateCrc16(Arrays.copyOfRange(encoded, 0, 14));
        assertArrayEquals(init_crc16, actual_crc16);
    }

    @Test
    public void testIllegalAlgorythm(){

    }
}
