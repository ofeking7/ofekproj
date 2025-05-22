package com.example.ofekrealpro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        datePicker = findViewById(R.id.datePicker);
        Button buttonStartTest = findViewById(R.id.buttonStartTest);

        // Set up the spinner for subjects
        String[] subjects = {"Math", "Science", "History", "English", "Art", "Music", "Geography", "Physics", "Chemistry", "Biology", "Computer Science", "Economics", "Literature", "Philosophy", "Drama", "Psychology", "Sociology", "Political Science", "Law"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        // Start the test activity
        buttonStartTest.setOnClickListener(v -> {
            String selectedSubject = spinnerSubjects.getSelectedItem().toString();
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            String testDate = year + "-" + (month + 1) + "-" + day;

            // Fixed the intent to point to the correct activity from manifest
            Intent intent = new Intent(MainActivity2.this, MusicScreen.class);
            intent.putExtra("selectedSubject", selectedSubject);
            intent.putExtra("testDate", testDate);
            startActivity(intent);
        });
    }
}