package client_tests;

import org.example.CustomKey;
import src.client.MessageGenerator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MessageGeneratorUnitTest {
    @Test
    public void testEncodePacketIsCalled(){
        Assertions.assertEquals(MessageGenerator.generateMsg(new CustomKey()).getClass(), byte[].class);
    }
}
