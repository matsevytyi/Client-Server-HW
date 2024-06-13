package server_tests;

import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.server.MTDecoder;




public class MTDecoderUnitTest {

    private CustomKey customKey;
    private byte[] packet;
    private String recipientIP;
    private String message;


    @BeforeEach
    public void setUp() {
        customKey = new CustomKey();
        packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "ping").toBytes()).toBytes();
        recipientIP = "127.0.0.1";
    }

    @Test
    public void testEncodeStartsNewThread() throws InterruptedException {
        int prev_count = Thread.activeCount();
        MTDecoder.decode(packet, customKey, recipientIP);
        Thread.activeCount();

        Assertions.assertTrue(prev_count < Thread.activeCount());
    }
}