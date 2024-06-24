package org.example;

import java.security.SecureRandom;

//Use to generate secret key
public class SecretKeyGenerator {

    public static void main(String[] args) {
        // Generate a 256-bit (32-byte) random key
        byte[] bytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        // Convert byte array to a hex string (optional step)
        String hexKey = bytesToHex(bytes);

        System.out.println("Generated Secret Key (256-bit): " + hexKey);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
