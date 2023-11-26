package com.com.student_management.utils;

import java.util.ArrayList;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static StringBuilder[] convertString(ArrayList<String> list) {
        StringBuilder[] stringBuilder = new StringBuilder[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String[] words = list.get(i).split("\\s+");
            stringBuilder[i] = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    stringBuilder[i].append(word.charAt(0));
                }
            }
        }
        return stringBuilder;
    }

    public static String convertString(String str) {
        String[] words = str.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                stringBuilder.append(word.charAt(0));
            }
        }
        return stringBuilder.toString().toUpperCase();
    }
}

