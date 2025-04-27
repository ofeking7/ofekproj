package com.example.ofekrealpro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSubjects;
    private DatePicker datePicker;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        datePicker = findViewById(R.id.datePicker);
        Button buttonStartTest = findViewById(R.id.buttonStartTest);

        // Set up the spinner with subject options
        String[] subjects = {
                "Math", "Science", "History", "English", "Art", "Music",
                "Geography", "Physics", "Chemistry", "Biology", "Computer Science",
                "Economics", "Literature", "Philosophy", "Drama", "Psychology",
                "Sociology", "Political Science", "Law"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, subjects
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        // Button click logic
        buttonStartTest.setOnClickListener(v -> {
            // Play click sound
            if (mediaPlayer == null) {
          //      mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.button_click); // put your sound in res/raw/
            }
          //  mediaPlayer.start();

            // Get selected subject and date
            String selectedSubject = spinnerSubjects.getSelectedItem().toString();
            int year = datePicker.getYear();
            int month = datePicker.getMonth(); // 0-based
            int day = datePicker.getDayOfMonth();

            // Format date properly
            String formattedMonth = String.format("%02d", month + 1);
            String formattedDay = String.format("%02d", day);
            String testDate = year + "-" + formattedMonth + "-" + formattedDay;

            // Start next activity and pass data
            Intent intent = new Intent(MainActivity.this, MusicScreen.class);
            intent.putExtra("selectedSubject", selectedSubject);
            intent.putExtra("testDate", testDate);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
