package com.example.ofekrealpro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StopwatchActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running = false;
    private final Handler handler = new Handler();
    private TextView textViewTime;
    private Button buttonStartStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        textViewTime = findViewById(R.id.textViewTime);
        buttonStartStop = findViewById(R.id.buttonStartStop);
        Button buttonReset = findViewById(R.id.buttonReset);


        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (running) {
                    running = false;
                    buttonStartStop.setText("Start");
                } else {
                    running = true;
                    buttonStartStop.setText("Stop");
                    runStopwatch();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                running = false;
                seconds = 0;
                textViewTime.setText("00:00");
                buttonStartStop.setText("Start");
            }
        });
    }

    private void runStopwatch() {
        handler.postDelayed(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (running) {
                    seconds++;
                    int minutes = seconds / 60;
                    int sec = seconds % 60;
                    textViewTime.setText(String.format("%02d:%02d", minutes, sec));
                    runStopwatch();
                }
            }
        }, 1000);
    }

}
