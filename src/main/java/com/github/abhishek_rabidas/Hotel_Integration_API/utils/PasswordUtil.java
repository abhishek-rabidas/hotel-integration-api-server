package com.github.abhishek_rabidas.Hotel_Integration_API.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordUtil {

    // converting plain text string to base64
    public static String convertToBase64(String key) {
        byte[] decodedBytes = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        return new String(decodedBytes);
    }

    // converting base64 value to plain text string
    public static String convertBase64ToString(String key) {
        byte[] decodedBytes = Base64.getDecoder().decode(key);
        return new String(decodedBytes);
    }
}
