package com.example.ofekrealpro;

import androidx.annotation.NonNull;

public class TestModel {
  //  "Math Exam", "2025-03-20", "Upcoming"
    private String testName;
    private String testDate;
    private String testStatus;

    public TestModel(String testName, String testDate, String testStatus) {
        this.testName = testName;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "TestModel{" +
                "testName='" + testName + '\'' +
                ", testDate='" + testDate + '\'' +
                ", testStatus='" + testStatus + '\'' +
                '}';
    }
}
