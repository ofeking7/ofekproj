package com.example.ofekrealpro;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StopwatchActivity extends AppCompatActivity {

    private TextView tvTimer, tvStatus;
    private Button btnStart, btnPause, btnReset;
    private Button btn10min, btn15min, btn20min, btn30min, btn1hour, btn90min, btnCustom;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // Default 1 minute
    private long totalTimeInMillis = 60000;
    private boolean timerRunning = false;
    private boolean timerPaused = false;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        initViews();
        setupClickListeners();
        updateTimerDisplay();
        updateProgressBar();
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_timer);
        tvStatus = findViewById(R.id.tv_status);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);
        progressBar = findViewById(R.id.progress_bar);

        // Time option buttons - updated to match new options
        btn10min = findViewById(R.id.btn_10m);
        btn15min = findViewById(R.id.btn_15min);
        btn20min = findViewById(R.id.btn_20min);
        btn30min = findViewById(R.id.btn_30min);
        btn1hour = findViewById(R.id.btn_1hour);
        btn90min = findViewById(R.id.btn_90min);
        btnCustom = findViewById(R.id.btn_custom);

        // Set initial state
        tvStatus.setText("Ready to start");
        btnPause.setVisibility(View.GONE);
        progressBar.setMax(100);
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());

        // Time option buttons - updated with new times
        btn10min.setOnClickListener(v -> setTime(10 * 60 * 1000));
        btn15min.setOnClickListener(v -> setTime(15 * 60 * 1000));
        btn20min.setOnClickListener(v -> setTime(20 * 60 * 1000));
        btn30min.setOnClickListener(v -> setTime(30 * 60 * 1000));
        btn1hour.setOnClickListener(v -> setTime(60 * 60 * 1000));
        btn90min.setOnClickListener(v -> setTime(90 * 60 * 1000));
        btnCustom.setOnClickListener(v -> showCustomTimeDialog());
    }

    private void setTime(long milliseconds) {
        if (!timerRunning) {
            timeLeftInMillis = milliseconds;
            totalTimeInMillis = milliseconds;
            updateTimerDisplay();
            updateProgressBar();
            tvStatus.setText("Ready to start");
            highlightSelectedButton(milliseconds);
        } else {
            Toast.makeText(this, "Stop timer first to change time", Toast.LENGTH_SHORT).show();
        }
    }

    private void highlightSelectedButton(long milliseconds) {
        // Reset all buttons to default color
        resetButtonColors();

        // Highlight selected button - updated for new options
        int highlightColor = ContextCompat.getColor(this, android.R.color.holo_blue_light);

        if (milliseconds == 10 * 60 * 1000) {
            btn10min.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else if (milliseconds == 15 * 60 * 1000) {
            btn15min.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else if (milliseconds == 20 * 60 * 1000) {
            btn20min.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else if (milliseconds == 30 * 60 * 1000) {
            btn30min.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else if (milliseconds == 60 * 60 * 1000) {
            btn1hour.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else if (milliseconds == 90 * 60 * 1000) {
            btn90min.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        } else {
            btnCustom.setBackgroundTintList(ColorStateList.valueOf(highlightColor));
        }
    }

    private void resetButtonColors() {
        int defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        btn10min.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btn15min.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btn20min.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btn30min.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btn1hour.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btn90min.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btnCustom.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
    }

    private void startTimer() {
        if (timeLeftInMillis <= 0) {
            Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show();
            return;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
                updateProgressBar();

                // Change color when time is running low
                if (millisUntilFinished <= 10000) { // Last 10 seconds
                    tvTimer.setTextColor(Color.RED);
                    tvStatus.setText("Time running out!");
                    tvStatus.setTextColor(Color.RED);
                } else if (millisUntilFinished <= 30000) { // Last 30 seconds
                    tvTimer.setTextColor(Color.YELLOW);
                    tvStatus.setText("Almost done!");
                    tvStatus.setTextColor(Color.YELLOW);
                } else {
                    tvTimer.setTextColor(ContextCompat.getColor(StopwatchActivity.this, android.R.color.black));
                    tvStatus.setText("Timer running...");
                    tvStatus.setTextColor(ContextCompat.getColor(StopwatchActivity.this, android.R.color.black));
                }
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timerPaused = false;
                timeLeftInMillis = 0;
                updateTimerDisplay();
                updateProgressBar();
                tvStatus.setText("Time's up!");
                tvStatus.setTextColor(Color.RED);
                tvTimer.setTextColor(Color.RED);

                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);

                // Play completion sound (you can add sound file to res/raw/)
                playCompletionSound();

                Toast.makeText(StopwatchActivity.this, "⏰ Timer finished!", Toast.LENGTH_LONG).show();
            }
        }.start();

        timerRunning = true;
        timerPaused = false;
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        timerPaused = true;
        tvStatus.setText("Timer paused");
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        timerPaused = false;
        timeLeftInMillis = totalTimeInMillis;
        updateTimerDisplay();
        updateProgressBar();
        tvStatus.setText("Ready to start");
        tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        tvTimer.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted;
        if (hours > 0) {
            timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeFormatted = String.format("%02d:%02d", minutes, seconds);
        }

        tvTimer.setText(timeFormatted);
    }

    private void updateProgressBar() {
        if (totalTimeInMillis > 0) {
            int progress = (int) ((totalTimeInMillis - timeLeftInMillis) * 100 / totalTimeInMillis);
            progressBar.setProgress(progress);
        }
    }

    private void showCustomTimeDialog() {
        // Simple custom time input - you can expand this with a proper dialog
        Toast.makeText(this, "Custom time feature - you can implement TimePickerDialog here", Toast.LENGTH_LONG).show();
        // For now, set a custom 2-minute timer as example
        setTime(2 * 60 * 1000);
    }

    private void playCompletionSound() {
        try {
            // You can add a sound file to res/raw/ folder and use it
            // mediaPlayer = MediaPlayer.create(this, R.raw.timer_sound);
            // mediaPlayer.start();

            // For now, use system notification sound
            android.media.RingtoneManager.getRingtone(
                    this,
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
            ).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}