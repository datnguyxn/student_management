package com.com.student_management.entities;

import android.net.Uri;

import com.com.student_management.constants.Roles;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String uuid;
    private String name;
    private Integer age;
    private String password;
    private String phone;
    private String email;
    private Roles role;
    private Uri avatar;
    private String status;
    private ArrayList<Date> history;

    //getters and setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Date> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Date> history) {
        this.history = history;
    }


    public User(String uuid, String name, Integer age, String phoneNumber, String password, String email, String role, String status, ArrayList<Date> history) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
        this.phone = phoneNumber;
        this.password = password;
        this.email = email;
        this.role = Roles.valueOf(role);
        this.status = status;
        this.history = history;
    }

    public User(HashMap<String, Object> userMap) {
        this.uuid = (String) userMap.get("uuid");
        this.name = (String) userMap.get("name");
        this.age = (Integer) userMap.get("age");
        this.phone = (String) userMap.get("phone");
        this.email = (String) userMap.get("email");
        this.password = (String) userMap.get("password");
        this.role = (Roles) userMap.get("role");
        this.status = (String) userMap.get("status");
        this.history = (ArrayList<Date>) userMap.get("history");
        this.avatar = Uri.parse((String) userMap.get("avatar"));
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (avatar == null) {
            result.put("uuid", uuid);
            result.put("name", name);
            result.put("phone", phone);
            result.put("email", email);
            result.put("password", password);
            result.put("role", role);
            result.put("age", age);
            result.put("status", status);
            result.put("history", history);
            result.put("avatar", "");
        } else {
            result.put("uuid", uuid);
            result.put("name", name);
            result.put("phone", phone);
            result.put("email", email);
            result.put("password", password);
            result.put("role", role);
            result.put("age", age);
            result.put("status", status);
            result.put("history", history);
            result.put("avatar", avatar.toString());
        }
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar=" + avatar.toString() +
                ", status='" + status + '\'' +
                ", history=" + history +
                ", role='" + role + '\'' +
                '}';
    }
}

