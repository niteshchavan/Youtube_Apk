package com.nitesh.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;


public class MainActivity extends AppCompatActivity {
    MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;

    private static final String LOG_TAG = "MainActivity";


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


    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    // Handle headset button press event
                    if (mediaSession != null) {
                        MediaControllerCompat mediaController = mediaSession.getController();
                        PlaybackStateCompat playbackState = mediaController.getPlaybackState();
                        if (playbackState != null) {
                            switch (playbackState.getState()) {
                                case PlaybackStateCompat.STATE_PLAYING:
                                    mediaController.getTransportControls().pause();
                                    break;
                                case PlaybackStateCompat.STATE_PAUSED:
                                case PlaybackStateCompat.STATE_STOPPED:
                                    mediaController.getTransportControls().play();
                                    break;
                            }
                        }
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private static class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            // Implement your logic for handling the play command
        }

        @Override
        public void onPause() {
            // Implement your logic for handling the pause command

        }

        @Override
        public void onStop() {
            // Implement your logic for handling the stop command
        }

        // Add more overridden methods as per your requirements

    }
}