package com.example.ofekrealpro;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerAndMusicActivity extends AppCompatActivity {

    private TextView textViewSubject, textViewTestDate, textViewTimer;
    private Button buttonStartTimer;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_and_music);

        textViewSubject = findViewById(R.id.textViewSubject);
        textViewTestDate = findViewById(R.id.textViewTestDate);
        textViewTimer = findViewById(R.id.textViewTimer);
        buttonStartTimer = findViewById(R.id.buttonStartTimer);

        String subject = getIntent().getStringExtra("selectedSubject");
        String testDate = getIntent().getStringExtra("testDate");

        textViewSubject.setText("Subject: " + subject);
        textViewTestDate.setText("Test Date: " + testDate);

        mediaPlayer = MediaPlayer.create(this, R.raw.music1); 
        mediaPlayer = MediaPlayer.create(this, R.raw.music2);
        buttonStartTimer.setOnClickListener(v -> {
            startTimer();
            playMusic();
        });
    }

    private void startTimer() {
        // Start a 10-minute countdown timer (600,000 ms = 10 minutes)
        countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                textViewTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("Time's Up!");
                mediaPlayer.stop();
            }
        }.start();
    }

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
