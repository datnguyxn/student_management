package com.com.student_management.entities;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean gender;
    private String birthday;
    private String phone;
    private String address;

    public Student() {
    }

    public Student(String id, String firstName, String lastName, String email, boolean gender, String birthday, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    public Student(String firstName, String lastName, String email, boolean gender, String birthday, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String uuid) {
        this.id = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("gender", gender);
        result.put("birthday", birthday);
        result.put("phone", phone);
        result.put("address", address);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
