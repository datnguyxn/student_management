package com.com.student_management.entities;

import android.net.Uri;

import com.com.student_management.utils.ArrayUtil;

import org.json.JSONArray;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String id;
    private String fullName;
    private String email;
    private boolean gender;
    private String birthday;
    private String phone;
    private String major;
    private String address;
    private String dateCreated;
    private ArrayList<String> dateUpdated;
    private String expirationDate;
    private ArrayList<String> idCertificate;

    public Student() {
    }

    public Student(String id, String fullName, String email, boolean gender, String birthday, String phone, String address, String major, String dateCreated, ArrayList<String> dateUpdated, ArrayList<String> idCertificate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.major = major;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.idCertificate = idCertificate;
    }

    public Student(String fullName, String email, boolean gender, String birthday, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    public Student(String studentId, String name, String birthday, String email, String phone, String major, String address, boolean isMale, ArrayList<String> dateUpdated) {
        this.id = studentId;
        this.fullName = name;
        this.email = email;
        this.gender = isMale;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.major = major;
        this.dateUpdated = dateUpdated;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String uuid) {
        this.id = uuid;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(boolean gender) {this.gender = gender;}

    public boolean getGender() {return gender;}

    public void setBirthday(String birthday) {this.birthday = birthday;}

    public String getBirthday() {return birthday;}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public String getMajor() {return major;}
    public void setMajor(String major) {this.major = major;}

    public void setAddress(String address) {
        this.address = address;
    }
    public void setDateCreated(String dateCreated) {this.dateCreated = dateCreated;}

    public String getDateCreated() {return dateCreated;}

    public void setDateUpdated(ArrayList<String> dateUpdated) {this.dateUpdated = dateUpdated;}

    public ArrayList<String> getDateUpdated() {return dateUpdated;}

    public void setIdCertificate(ArrayList<String> idCertificate) {this.idCertificate = idCertificate;}

    public ArrayList<String> getIdCertificate() {return idCertificate;}

    public Student(HashMap<String, Object> studentMap) {
        this.id = (String) studentMap.get("id");
        this.fullName = (String) studentMap.get("fullName");
        this.email = (String) studentMap.get("email");
        this.gender = (boolean) studentMap.get("gender");
        this.birthday = (String) studentMap.get("birthday");
        this.phone = (String) studentMap.get("phone");
        this.address = (String) studentMap.get("address");
        this.major = (String) studentMap.get("major");
        this.dateCreated = (String) studentMap.get("dateCreated");
        this.dateUpdated = ArrayUtil.convert((JSONArray) studentMap.get("dateUpdated"));
        this.idCertificate = ArrayUtil.convert((JSONArray) studentMap.get("idCertificate"));

    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("gender", gender);
        result.put("birthday", birthday);
        result.put("phone", phone);
        result.put("address", address);
        result.put("major", major);
        result.put("dateCreated", dateCreated);
        result.put("dateUpdated", dateUpdated);
        result.put("idCertificate", idCertificate);
        return result;
    }
    public Map<String, Object> updateUserToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("gender", gender);
        result.put("birthday", birthday);
        result.put("phone", phone);
        result.put("address", address);
        result.put("major", major);
        result.put("dateUpdated", dateUpdated);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", major='" + major + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", idCertificate='" + idCertificate + '\'' +
                '}';
    }
}
