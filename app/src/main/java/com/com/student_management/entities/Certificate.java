package com.com.student_management.entities;


import com.com.student_management.utils.ArrayUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Certificate {
    private String id;
    private String name;
    private String description;
    private String dateCreated;
    private ArrayList<String> dateUpdated;

    public Certificate() {
    }

    public Certificate(String id, String name, String description, String dateCreated, ArrayList<String> dateUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Certificate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Certificate(String name, String description, String dateCreated, ArrayList<String> dateUpdated) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Certificate(String nameCertificate, String descriptionCertificate, ArrayList<String> dateUpdated) {
        this.name = nameCertificate;
        this.description = descriptionCertificate;
        this.dateUpdated = dateUpdated;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public Certificate(HashMap<String, Object> certificateMap) {
        this.id = (String) certificateMap.get("id");
        this.name = (String) certificateMap.get("name");
        this.description = (String) certificateMap.get("description");
        this.dateCreated = (String) certificateMap.get("dateCreated");
        this.dateUpdated = ArrayUtil.convert((JSONArray) certificateMap.get("dateUpdated"));
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("dateCreated", dateCreated);
        result.put("dateUpdated", dateUpdated);
        return result;
    }

    public Map<String, Object> updateCertificateToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("dateUpdated", dateUpdated);
        return result;
    }

    public String toStringToCSV() {
        return id + "," + name + "," + description + "," + dateCreated + "," + dateUpdated;
    }
    public String toStringToCSVForCertificate() {
        return id + "," + name + "," + description + "," + dateCreated;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                '}';
    }
}
