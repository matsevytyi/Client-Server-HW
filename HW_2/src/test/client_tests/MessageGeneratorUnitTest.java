package client_tests;

import src.client.MessageGenerator;
import src.packet_handling.CustomKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MessageGeneratorUnitTest {
    @Test
    public void testEncodePacketIsCalled(){
        Assertions.assertEquals(MessageGenerator.generateMsg(new CustomKey()).getClass(), byte[].class);
    }
}
