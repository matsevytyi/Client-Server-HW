package threads_tests;

import org.example.CustomKey;
import org.example.Packet;
import org.example.PacketDecoder;
import src.custom_threads.DecoderThread;
import src.server.MTProcessor;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class DecoderThreadUnitTest {

    @BeforeEach
    public void setUp() {
        // Error will be printed in Decoder class because I inserted mock data
        // Below this print is being disabled.

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void isLaunched(){
        DecoderThread mockThread = new DecoderThread(new byte[20], new CustomKey(), "127.0.0.1");
        Assertions.assertTrue(mockThread.isAlive());
    }

    @Test
    public void testDecodePacketIsCalled() {
        // Mock the static method
        try (MockedStatic<PacketDecoder> mockedDecoder = Mockito.mockStatic(PacketDecoder.class)) {
            Packet mockPacket = mock(Packet.class);

            mockedDecoder.when(() -> PacketDecoder.decodePacket(any(byte[].class), any(javax.crypto.SecretKey.class)))
                    .thenReturn(mockPacket);

            DecoderThread decoderThread = new DecoderThread(new byte[20], new CustomKey(), "127.0.0.1");
            decoderThread.run();

            // Verify that the static method was called
            mockedDecoder.verify(() -> PacketDecoder.decodePacket(any(byte[].class), any(javax.crypto.SecretKey.class)), times(1));
        }
    }

    @Test
    public void testMTProcessorProcessIsCalled() {
        // Mock the static methods
        try (MockedStatic<PacketDecoder> mockedPacketDecoder = Mockito.mockStatic(PacketDecoder.class);
             MockedStatic<MTProcessor> mockedMTProcessor = Mockito.mockStatic(MTProcessor.class)) {

            Packet mockPacket = mock(Packet.class);
            mockedPacketDecoder.when(() -> PacketDecoder.decodePacket(any(byte[].class), any(javax.crypto.SecretKey.class)))
                    .thenReturn(mockPacket);

            DecoderThread decoderThread = new DecoderThread(new byte[20], new CustomKey(), "127.0.0.1");
            decoderThread.run();

            // Verify that the process method was called
            mockedMTProcessor.verify(() -> MTProcessor.process(any(Packet.class), any(CustomKey.class), any(String.class)), times(1));
        }
    }

}
