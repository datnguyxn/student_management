package com.com.student_management.utils;

import android.net.Uri;

import com.com.student_management.entities.Student;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    private InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

//    private void readCSVFile(Uri uri) {
//        try {
//            InputStream inputStream =
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Process each line of the CSV file
//                String[] values = line.split(",");
//                // Do something with the values
//            }
//
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
