package threads_tests;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketEncoder;
import src.custom_threads.EncoderThread;
import src.server.Sender;



import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class EncoderThreadUnitTest {
    @BeforeEach
    public void setUp() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void isLaunched(){
        Packet mockPacket = mock(Packet.class);
        EncoderThread mockThread = new EncoderThread(mockPacket, new CustomKey(), "127.0.0.1", "somemessage");
        Assertions.assertTrue(mockThread.isAlive());
    }

    @Test
    public void testEncodePacketIsCalled() {
        try(MockedStatic<PacketEncoder> mockedEncoder = Mockito.mockStatic(PacketEncoder.class)){

            byte[] mockEncoded = new byte[20];
            mockedEncoder.when(() -> PacketEncoder.encodePacket(any(Packet.class), any(javax.crypto.SecretKey.class))).thenReturn(mockEncoded);

            EncoderThread encoderThread = new EncoderThread(mock(Packet.class), new CustomKey(), "127.0.0.1", "somemessage");
            encoderThread.run();

            mockedEncoder.verify(() -> PacketEncoder.encodePacket(any(Packet.class), any(javax.crypto.SecretKey.class)), times(1));
        }
    }

    @Test
    public void testSenderProcessIsCalled() {
        try(MockedStatic<Sender> mockedSender = Mockito.mockStatic(Sender.class);
        MockedStatic<PacketEncoder> mockedEncoder = Mockito.mockStatic(PacketEncoder.class)){
            byte[] mockEncoded = new byte[20];
            mockedEncoder.when(() -> PacketEncoder.encodePacket(any(Packet.class), any(javax.crypto.SecretKey.class))).thenReturn(mockEncoded);

            EncoderThread encoderThread = new EncoderThread(mock(Packet.class), new CustomKey(), "127.0.0.1", "somemessage");
            encoderThread.run();

            mockedSender.verify(() -> Sender.send(any(byte[].class), any(String.class), any(String.class)), times(1));
        }
    }
}
