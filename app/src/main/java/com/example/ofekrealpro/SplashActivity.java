package com.example.ofekrealpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        VideoView videoView = findViewById(R.id.splashVideoView);

        // Note: Firebase initialization is now handled in MyApplication class
        // We've removed FirebaseApp.initializeApp(this) from here

        // Set the video path
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreen);
        videoView.setVideoURI(videoUri);

        // Start playing the video
        videoView.start();

        // Add listener to detect when the video ends
        videoView.setOnCompletionListener(mp -> {
            // Navigate to MainActivity after the video ends
            Intent intent = new Intent(SplashActivity.this, AddTestActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle case where the user skips the splash screen (optional)
        videoView.setOnErrorListener((mp, what, extra) -> {
            Intent intent = new Intent(SplashActivity.this, AddTestActivity.class);
            startActivity(intent);
            finish();
            return true;
        });
    }
}