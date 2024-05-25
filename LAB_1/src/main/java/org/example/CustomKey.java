package org.example;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

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

    public SecretKey getSecretKey() {
        return this.secretKey;
    }


}
