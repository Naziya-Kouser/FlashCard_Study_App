package com.example.study;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video); // This layout will contain the VideoView

        // Initialize VideoView
        videoView = findViewById(R.id.video_view);

        // Set the video URI
        videoView.setVideoURI(android.net.Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loading));

        // Set a listener to start MainActivity after the video finishes
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Start MainActivity after video ends
                Intent intent = new Intent(VideoActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close VideoActivity
            }
        });

        // Start the video
        videoView.start();
    }
}
