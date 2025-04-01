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
    private static String encodeBytesAsBase32(byte[] values) {
        int bitSelector = 5;
        int current = 0;
        StringBuilder result = new StringBuilder("");
        for (byte val : values) {
            current += (int) ((int) (val >> (8 - bitSelector)) & 0xFF);
            result.append(BASE32_ALPHABET.charAt(current));
            current = (int) (((int) val << bitSelector) & 0xFF);
            current = (int) (((int) current >> 3) & 0xFF);
            // bitselector can have more than 3 uses within the same byte
            if ((8 - bitSelector) < 5) {
                bitSelector = 5 - (8 - bitSelector);
                continue;
            }
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

    private static String encodeLongAsBase32(long timestamp) {
        long timestamp48bits = timestamp & 0xFFFFFFFFFFFFL;
        long current = 0;
        final int selectedBits = 5;
        int totalBits = 48;
        StringBuilder result = new StringBuilder("");
        while (totalBits >= selectedBits) {
            current = (long) ( (long) ( timestamp48bits >> (totalBits - selectedBits) ));
            current = current & 0x1F; // extract selected bits using a mask, i.e., the last 5 bits
            result.append(BASE32_ALPHABET.charAt((int)current));
            totalBits -= selectedBits;
        }
        if (totalBits < selectedBits && totalBits > 0) {
            current = timestamp48bits & 0x07; // extract last 3 bits
            current = current << 2; // move 2 positions to the left, so that it is 5 bits now
            result.append(BASE32_ALPHABET.charAt((int)current));
        }
        return result.toString();
    }
    
    public static String getULID() {
        StringBuilder secureValue = new StringBuilder("");
        long currentTimestampMilliSecond = Instant.now().toEpochMilli(); // current utc timestamp; the first 48 bits
        // secureValue += currentTimestampMilliSecond;
        byte[] tempSecureRandomValues = new byte[10];
        SecureRandom generateSecureRandomValue = new SecureRandom();
        generateSecureRandomValue.nextBytes(tempSecureRandomValues); // the secure and randomly generated 80 bits
        secureValue.append(encodeLongAsBase32(currentTimestampMilliSecond));
        secureValue.append(encodeBytesAsBase32(tempSecureRandomValues));
        return secureValue.toString();
    }
}
