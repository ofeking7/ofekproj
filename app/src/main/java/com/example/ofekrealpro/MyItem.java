package com.example.ofekrealpro;

public class MyItem {
    private final String title;
    private final String description;

    public MyItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}