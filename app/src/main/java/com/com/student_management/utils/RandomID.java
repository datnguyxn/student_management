package com.com.student_management.utils;

import java.util.Date;
import java.util.Random;

public class RandomID {
    public static String generateIDStudent() {
        // Create an instance of the Random class
        Random random = new Random();

        // Generate a random 7-digit number (since we want 8 digits in total)
        long sevenDigits = Long.parseLong(FormatDateTime.formateDate(new Date())) * 100000 + random.nextInt(90000);

        // Add 5 at the beginning to make it start with 5
        return String.valueOf(50000000 + sevenDigits);
    }

    public static String generateIDCertificate() {
        Random random = new Random();
        long sevenDigits = Long.parseLong(FormatDateTime.formateDate(new Date())) * 1000000 + random.nextInt(90000);
        return String.valueOf(sevenDigits);
    }
}
