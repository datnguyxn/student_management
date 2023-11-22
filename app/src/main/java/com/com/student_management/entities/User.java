package com.com.student_management.entities;

import android.net.Uri;


import com.com.student_management.utils.ArrayUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String uuid;
    private String name;
    private Long age;
    private String password;
    private String phone;
    private String email;
    private String role;
    private Uri avatar;
    private String status;
    private ArrayList<String> history;

    public User() {
    }

    public User(String uuid, String name, Long age, String email, String role, Uri avatar, String status, ArrayList<String> history) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = String.valueOf(role);
        this.avatar = avatar;
        this.status = status;
        this.history = history;
    }
    public User(String name, Long age, String email, String password, String phone, String role, String status, ArrayList<String> history) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.history = history;
    }

    public User(String name, String email, Long age, String phone, String role, String status) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
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

    public String getRole() {
        return role.toString();
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getAvatar() {
        return this.avatar.toString();
    }


    public void setAvatar(String avatar) {
        this.avatar = Uri.parse(avatar);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }


    public User(String uuid, String name, Long age, String phoneNumber, String password, String email, String role, String status, ArrayList<String> history) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
        this.phone = phoneNumber;
        this.password = password;
        this.email = email;
        this.role = String.valueOf(role);
        this.status = status;
        this.history = history;
    }

    public User(HashMap<String, Object> userMap) {
        this.uuid = (String) userMap.get("uuid");
        this.name = (String) userMap.get("name");
        this.age = (Long) userMap.get("age");
        this.phone = (String) userMap.get("phone");
        this.email = (String) userMap.get("email");
        this.password = (String) userMap.get("password");
        this.role = (String) userMap.get("role");
        this.status = (String) userMap.get("status");
        this.history = ArrayUtil.convert((JSONArray) userMap.get("history"));
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
//            result.put("uuid", uuid);
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

    public Map<String, Object> updateUserToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phone", phone);
        result.put("email", email);
        result.put("role", role);
        result.put("age", age);
        result.put("status", status);
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
                ", avatar=" + avatar +
                ", status='" + status + '\'' +
                ", history=" + history +
                ", role='" + role + '\'' +
                '}';
    }
}

