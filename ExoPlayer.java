package com.example.exoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.os.Bundle;



public class MainActivity extends AppCompatActivity {
        private ExoPlayer player;
        private long playbackPosition;
        private boolean playWhenReady;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            PlayerView playerView = findViewById(R.id.player_view);
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri("https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4");
            player.setMediaItem(mediaItem);
            // Prepare the player
            player.prepare();
            // Start the playback
            player.play();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            if (player != null) {
                outState.putLong("player_position", player.getCurrentPosition());
                outState.putBoolean("player_playing", player.isPlaying());
            }
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            if (player != null) {
                long position = savedInstanceState.getLong("player_position");
                boolean playing = savedInstanceState.getBoolean("player_playing");
                player.seekTo(position);
                if (playing) {
                    player.play();
                } else {
                    player.pause();
                }
            }
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            playWhenReady = player.isPlaying();
            player.release();
            player = null;
        }
    }
    }
