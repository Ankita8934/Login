package com.service.login.auth.Utils;

public class AppUtil {

    public static String generateAlphaNumericString(int n) {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            stringBuilder.append(alphaNumericString.charAt(index));
        }
        return stringBuilder.toString();
    }
}
