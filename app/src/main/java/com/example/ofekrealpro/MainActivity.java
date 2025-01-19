package com.example.ofekrealpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private DatePicker datePicker;
    private Button buttonStartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        datePicker = findViewById(R.id.datePicker);
        buttonStartTest = findViewById(R.id.buttonStartTest);

        String[] subjects = {"Math", "Science", "History", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        buttonStartTest.setOnClickListener(v -> {
            String selectedSubject = spinnerSubjects.getSelectedItem().toString();

            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            String testDate = year + "-" + (month + 1) + "-" + day;

            Intent intent = new Intent(MainActivity.this, TimerAndMusicActivity.class);
            intent.putExtra("selectedSubject", selectedSubject);
            intent.putExtra("testDate", testDate);
            startActivity(intent);
        });
    }
}
