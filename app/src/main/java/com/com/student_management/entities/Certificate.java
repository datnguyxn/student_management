package com.com.student_management.entities;


import java.util.HashMap;
import java.util.Map;

public class Certificate {
    private String id;
    private String name;
    private String description;
    private String expirationDate;
    private String studentId;

    public Certificate() {
    }

    public Certificate(String id, String name, String description, String expirationDate, String studentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.studentId = studentId;
    }

    public Certificate(String name, String description, String expirationDate, String studentId) {
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
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


    public String getExpirationDate() {
        return expirationDate;
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


    public void setExpirationDate(String expirationDate) {
        this.expirationDate = Certificate.this.expirationDate;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("expirationDate", expirationDate);
        result.put("studentId", studentId);
        return result;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
