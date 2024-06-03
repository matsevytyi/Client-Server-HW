package threads_tests;

import src.custom_threads.ProcessorThread;
import src.packet_handling.CustomKey;
import src.packet_handling.Packet;
import src.server.MTEncoder;

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


public class ProcessorThreadUnitTest {

    @BeforeEach
    public void setUp() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void isLaunched(){
        ProcessorThread mockThread = new ProcessorThread(mock(Packet.class), new CustomKey(), "127.0.0.1");
        Assertions.assertTrue(mockThread.isAlive());
    }

    @Test
    public void testMTPEncoderIsCalled(){
        try (MockedStatic<MTEncoder> mockedEncoder = Mockito.mockStatic(MTEncoder.class)){

            ProcessorThread processorThread = new ProcessorThread(new Packet((byte) 1, new byte[4], new byte[20]), new CustomKey(), "127.0.0.1");
            processorThread.run();

            mockedEncoder.verify(() -> MTEncoder.encode(any(Packet.class), any(CustomKey.class), any(String.class), any(String.class)), times(1));
        }
    }
}
