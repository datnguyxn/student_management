package com.com.student_management.utils;

import android.util.Log;

import com.com.student_management.entities.Certificate;
import com.com.student_management.entities.Student;
import com.com.student_management.models.CertificateModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class CSVFile {
    private static final String TAG = "CSVFile";
    private InputStream inputStream;
    private OutputStream outputStream;
    private Student student;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public CSVFile(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public ArrayList<Student> readCSVForStudent() throws FileNotFoundException {
        ArrayList<Student> students = new ArrayList<>();
        try {
            ArrayList<String> nameCertificate = new ArrayList<>();
            ArrayList<String> idCertificate = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                boolean gender = false;
                // Process each line of the CSV file
                String[] values = line.split(",");
                if (values[1].equalsIgnoreCase("TRUE")) {
                    gender = true;
                }
                if (values.length > 6) {
                    for (int i = 6; i < values.length; i++) {
                        nameCertificate.add(values[i]);
                    }
                    Log.d(TAG, "readCSV: " + nameCertificate.toString());
                    student = new Student(values[0], gender, values[2], values[3], values[4], values[5], nameCertificate);
                    students.add(student);
                    nameCertificate = new ArrayList<>();
                } else {
                    Log.d(TAG, "readCSV: " + idCertificate.toString());
                    student = new Student(values[0], gender, values[2], values[3], values[4], values[5], idCertificate);
                    students.add(student);
                    Log.d(TAG, "readCSV: " + student.toString());
                }
                inputStream.close();
                // Do something with the values
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public ArrayList<Certificate> readCSVForCertificate() throws FileNotFoundException {
        ArrayList<Certificate> certificates = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Certificate certificate = new Certificate(values[0].toUpperCase(), values[1]);
                certificates.add(certificate);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificates;
    }
}
