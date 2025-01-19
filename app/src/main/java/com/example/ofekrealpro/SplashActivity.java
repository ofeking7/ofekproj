package com.example.ofekrealpro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        VideoView videoView = findViewById(R.id.splashVideoView);

        // Set the video path
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreen);
        videoView.setVideoURI(videoUri);

        // Start playing the video
        videoView.start();

        // Add listener to detect when the video ends
        videoView.setOnCompletionListener(mp -> {
            // Navigate to MainActivity after the video ends
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle case where the user skips the splash screen (optional)
        videoView.setOnErrorListener((mp, what, extra) -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        });
    }
}
