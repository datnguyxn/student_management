package com.com.student_management.entities;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class Certificate {
    private String id;
    private String name;
    private String description;
    private Uri image;
    private String date;
    private String studentId;

    public Certificate() {
    }

    public Certificate(String id, String name, String description, Uri image, String date, String studentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.studentId = studentId;
    }

    public Certificate(String name, String description, Uri image, String date, String studentId) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.studentId = studentId;
    }

    //getters & setters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("image", image);
        result.put("date", date);
        result.put("studentId", studentId);
        return result;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", date='" + date + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
