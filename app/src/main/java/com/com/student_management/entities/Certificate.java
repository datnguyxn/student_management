package com.com.student_management.entities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Certificate {
    private String id;
    private String name;
    private String description;
    private String expirationDate;
    private String studentId;
    private String dateCreated;
    private ArrayList<String> dateUpdated;

    public Certificate() {
    }

    public Certificate(String id, String name, String description, String expirationDate, String studentId, String dateCreated, ArrayList<String> dateUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.studentId = studentId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Certificate(String name, String description, String expirationDate) {
        this.name = name;
        this.description = description;
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

    public void setDateCreated(String dateCreated) {
        this.dateCreated = Certificate.this.dateCreated;
    }

    public void setDateUpdated(ArrayList<String> dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public ArrayList<String> getDateUpdated() {
        return dateUpdated;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("expirationDate", expirationDate);
        result.put("studentId", studentId);
        result.put("dateCreated", dateCreated);
        result.put("dateUpdated", dateUpdated);
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
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                '}';
    }
}
