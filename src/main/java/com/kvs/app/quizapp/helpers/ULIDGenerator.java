package com.kvs.app.quizapp.helpers;

import java.time.Instant;
import java.security.SecureRandom;

public class ULIDGenerator {

    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    // 5 bits are one value
    // start with a integer with zero value,
    // then put the first 5 bits from the byte into integer
    // interpret as a single value
    // note the remaining bits NOT USED (could be high as 8 bits)
    // in the next round first shift in the unused bit, 
    // only after it use bits from the remaining  space from 
    // current byte.
    public static String encodeBase32(byte[] values) {
        int bitSelector = 5;
        int current = 0;
        StringBuilder result = new StringBuilder("");
        for (byte val : values) {
            current += (int) ((int) (val >> (8 - bitSelector)) & 0xFF);
            // System.out.println("First spot: " + Integer.toString(current));
            // System.out.println(BASE32_ALPHABET.charAt(current));
            result.append(BASE32_ALPHABET.charAt(current));
            current = (int) (((int) val << bitSelector) & 0xFF);
            current = (int) (((int) current >> 3) & 0xFF);
            // bitselector can have more than 3 uses within the same byte
            if ((8 - bitSelector) < 5) {
                bitSelector = 5 - (8 - bitSelector);
                continue;
            }
            // current = (int) ((int) (current >> 3) & 0xFF);
            // System.out.println("Second spot: " + Integer.toString(current));
            // System.out.println(BASE32_ALPHABET.charAt(current));
            result.append(BASE32_ALPHABET.charAt(current));
            bitSelector = 8 - 5 - bitSelector;
            current = (int) (((int) val << (8 - bitSelector)) & 0xFF);
            current = (int) (((int) current >> 3) & 0xFF);
            bitSelector = 5 - bitSelector;
        }
        if (bitSelector < 5) {
            result.append(BASE32_ALPHABET.charAt(current));
        }
        return result.toString();
    }
    
    public static String getULID() {
        StringBuilder secureValue = new StringBuilder("");
        String currentTimestampMilliSecond = Long.toString(Instant.now().toEpochMilli()); // current utc timestamp; the first 48 bits
        // secureValue += currentTimestampMilliSecond;
        byte[] tempSecureRandomValues = new byte[10];
        SecureRandom generateSecureRandomValue = new SecureRandom();
        generateSecureRandomValue.nextBytes(tempSecureRandomValues); // the secure and randomly generated 80 bits
        secureValue.append(encodeBase32(tempSecureRandomValues));
        return secureValue.toString();
    }
}
