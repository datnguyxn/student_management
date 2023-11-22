package com.com.student_management.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateTime {
    public static String formatDateTime() {
        String pattern = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        System.out.println(date);
        return date;
    }
}
