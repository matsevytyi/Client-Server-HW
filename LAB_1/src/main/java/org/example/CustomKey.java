package org.example;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class CustomKey {

    private SecretKey secretKey;

    public CustomKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            int keyBitSize = 256;
            keyGenerator.init(keyBitSize, secureRandom);
            this.secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomKey(String filePath) {

        String encodedKey;
        try (Scanner scanner = new Scanner(new File(filePath))) {
            encodedKey = scanner.nextLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public CustomKey(String encodedKey, boolean isEncoded) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


    public String getEncodedKey() {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public void saveToFile(String filePath) {
        String encodedKey = getEncodedKey();

        // Write the Base64 String to a text file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(encodedKey);
            System.out.println("SecretKey written to file: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }


}
