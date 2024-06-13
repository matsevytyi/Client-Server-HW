package org.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet {

    private final byte bMagic = 0x13;
    private byte bSrc;
    private long bPktId; // 8 bytes
    private byte[] wLen; // 4 bytes

    private byte[] bMsg; // wLen bytes

    private byte[] wCrc16e; // 2 bytes

    public Packet(byte bSrc, long bPktId, byte[] message) {
        this.bSrc = bSrc;
        this.bPktId = bPktId; //ensure 8 bytes length
        int msgLen = message.length;
        this.wLen = new byte[]{(byte) ((msgLen >> 24) & 0xFF), (byte) ((msgLen >> 16) & 0xFF), (byte) ((msgLen >> 8) & 0xFF), (byte) (msgLen & 0xFF)}; //get from message to bytes
        this.bMsg = message;
        this.wCrc16e = calculateCrc16(bMsg);
    }

    public byte[] toBytes() {
        byte[] answer = new byte[18 + this.bMsg.length];
        answer[0] = this.bMagic;
        answer[1] = this.bSrc;
        System.arraycopy(ByteBuffer.allocate(8).putLong(this.bPktId).array(), 0, answer, 2, 8);
        System.arraycopy(this.wLen, 0, answer, 10, 4);
        System.arraycopy(calculateCrc16(Arrays.copyOfRange(answer, 0, 14)), 0, answer, 14, 2);
        System.arraycopy(this.bMsg, 0, answer, 16, this.bMsg.length);
        System.arraycopy(this.wCrc16e, 0, answer, 16 + this.bMsg.length, 2);
        return answer;
    }

    public Message getMessage() {
        return new Message(this.bMsg);
    }

    public byte getbSrc() {
        return this.bSrc;
    }

    public long getbPktId() {
        return this.bPktId;
    }

    public void setbPktId(long bPktId) {
        this.bPktId = bPktId;
    }

    public static byte[] calculateCrc16(byte[] data) {
        int crc = 0xFFFF; // initial value

        for (byte b : data) {
            crc ^= (b & 0xFF) << 8;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021; // standart polinomial
                } else {
                    crc <<= 1;
                }
            }
        }
        return new byte[]{(byte) ((crc >> 8) & 0xFF), (byte) (crc & 0xFF)};
    }
}
