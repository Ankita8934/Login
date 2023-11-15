package com.service.login.auth.utils;

import java.security.SecureRandom;

public class StrUtil {

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String formatPhoneHtmlLink(String number) {
        String formatted = null;
        if (number != null && !number.isEmpty()) {
            formatted = formatPhone(number);
            formatted = "<a href=\"tel://" + formatted + "\">" + formatted + "</a>";
        }
        return formatted;
    }

    public static String formatPhone(String number) {
        String formatted = null;
        if (number != null && !number.isEmpty()) {
            if (number.length() != 10) {
                formatted = number;
            } else {
                formatted = String.format("(%s) %s-%s", number.substring(0, 3), number.substring(3, 6),
                        number.substring(6, 10));
            }
        }
        return formatted;
    }
    public static String randomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(randomIndex));
        }

        return builder.toString();
    }


}
