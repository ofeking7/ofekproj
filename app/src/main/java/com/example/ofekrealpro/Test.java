package com.example.ofekrealpro;

public class Test {
    private String id;
    private String name;
    private String date;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    // Empty constructor required for Firestore
    public Test() {
    }

    // Constructor with parameters
    public Test(String id, String name, String date, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}