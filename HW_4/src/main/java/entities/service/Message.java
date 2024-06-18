package entities.service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Message {

    private int cType;
    private int bUserId;
    private String message;

    public Message(int cType, int userID, String message) {
        this.cType = cType;
        this.bUserId = userID;
        this.message = message;
    }

    public Message(byte[] bytes) {
        this.cType = ByteBuffer.wrap(bytes, 0, 4).getInt();
        bUserId = ByteBuffer.wrap(bytes, 4, 4).getInt();
        message = new String(Arrays.copyOfRange(bytes, 8, bytes.length));
    }

    public String toString() {
        return "Message{" +
                "cType=" + cType +
                ", bUserId=" + bUserId +
                ", message='" + message + '\'' +
                '}';
    }

    public byte[] toBytes() {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8); // Convert message to byte array

        ByteBuffer buffer = ByteBuffer.allocate(8 + messageBytes.length);
        buffer.putInt(cType);           // Put cType as 4 bytes
        buffer.putInt(bUserId);         // Put bUserId as 4 bytes
        buffer.put(messageBytes);       // Put message bytes

        return buffer.array();          // Get the underlying byte array
    }


    public int getcType() {
        return cType;
    }

    public int getbUserId() {
        return bUserId;
    }

    public String getMessage() {
        return this.message;
    }
}
