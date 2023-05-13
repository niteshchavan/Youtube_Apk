package com.nitesh.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.support.v4.media.session.*;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(this, LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller
        mediaSession.setCallback(new MySessionCallback());

        // Create a MediaControllerCompat
        MediaControllerCompat mediaController =
                new MediaControllerCompat(this, mediaSession);

        MediaControllerCompat.setMediaController(this, mediaController);
        Log.d("MainActivity", "dispatchKeyEvent called");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();

        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY && action == KeyEvent.ACTION_DOWN) {
            // Handle media play key down event
            Log.d("MainActivity", "Media play key down");
            return true; // Consumed the event
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE && action == KeyEvent.ACTION_DOWN) {
            // Handle media pause key down event
            Log.d("MainActivity", "Media pause key down");
            return true; // Consumed the event
        }

        // Return false for other key events to allow the system to handle them
        return super.dispatchKeyEvent(event);
    }

    private static class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {

            Log.d("MainActivity", "dispatchKeyEvent called onPlay");

            // Implement your logic for handling the play command
        }

        @Override
        public void onPause() {
            Log.d("MainActivity", "dispatchKeyEvent called onPause");
            // Implement your logic for handling the pause command

        }

        @Override
        public void onStop() {
            Log.d("MainActivity", "dispatchKeyEvent called onStop");
            // Implement your logic for handling the stop command
        }

        // Add more overridden methods as per your requirements

    }
}
