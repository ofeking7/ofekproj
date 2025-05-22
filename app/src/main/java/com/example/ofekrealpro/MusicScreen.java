package com.example.ofekrealpro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MusicScreen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicscreen);

        Button btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Buttons for music
        Button buttonPlaySong1 = findViewById(R.id.buttonPlaySong1);
        Button buttonPlaySong2 = findViewById(R.id.buttonPlaySong2);
        Button buttonPlaySong3 = findViewById(R.id.buttonPlaySong3);
        Button buttonStopwatch = findViewById(R.id.buttonStopwatch);

        // Set up button listeners for music playback
        buttonPlaySong1.setOnClickListener(v -> playSong(R.raw.music1));
        buttonPlaySong2.setOnClickListener(v -> playSong(R.raw.music2));
        buttonPlaySong3.setOnClickListener(v -> playSong(R.raw.song3));
        buttonStopwatch.setOnClickListener(v -> {
            Intent intent = new Intent(MusicScreen.this, StopwatchActivity.class);
            startActivity(intent);
        });

    }

    private void playSong(int songId) {
        // Stop any previously playing song
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Play new song
        mediaPlayer = MediaPlayer.create(this, songId);
        mediaPlayer.start();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}



