package com.shorten_url.helpers;

import org.springframework.stereotype.Service;

@Service
public class Base62Converter {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = ALPHABET.length(); // 62

    public String encode(long number) {
        if (number == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(ALPHABET.charAt((int) (number % BASE)));
            number /= BASE;
        }
        return sb.reverse().toString();
    }

    public long decode(String base62String) {
        long result = 0;
        long power = 1;
        for (int i = base62String.length() - 1; i >= 0; i--) {
            int digit = ALPHABET.indexOf(base62String.charAt(i));
            result += digit * power;
            power *= BASE;
        }
        return result;
    }
}

