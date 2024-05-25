package org.example;


import java.util.Arrays;

public class Message {

    private String cType;
    private String bUserId;
    private byte[] message;

    public Message(String cType, String userID, byte[] message) {
        this.cType = cType;
        this.bUserId = userID;
        this.message = message;
    }

    public Message(byte[] bytes) {
        cType = new String(Arrays.copyOfRange(bytes, 0, 4));
        bUserId = new String(Arrays.copyOfRange(bytes, 4, 8));
        message = Arrays.copyOfRange(bytes, 8, bytes.length);
    }

    public byte[] toBytes() {
        byte[] answer = new byte[8 + message.length];
        System.arraycopy(cType.getBytes(), 0, answer, 0, 4);
        System.arraycopy(bUserId.getBytes(), 0, answer, 4, 4);
        System.arraycopy(message, 0, answer, 8, message.length);
        return answer;
    }
}
