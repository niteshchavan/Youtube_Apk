
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.mapper.VideoInfo;

public class MainActivity extends AppCompatActivity {
    private ExoPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YoutubeDLRequest request = new YoutubeDLRequest("https://youtu.be/Pv61yEcOqpw");
        request.addOption("-f", "best");
        VideoInfo streamInfo = YoutubeDL.getInstance().getInfo(request);
        String videoUrl = streamInfo.getUrl();

        PlayerView playerView = findViewById(R.id.player_view);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        // Prepare the player
        player.prepare();
        // Start the playback
        player.play();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            PlayerView playerView = findViewById(R.id.player_view);
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(layoutParams);
        } else {
            PlayerView playerView = findViewById(R.id.player_view);
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 500;
            playerView.setLayoutParams(layoutParams);
        }
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
            long playbackPosition = player.getCurrentPosition();
            boolean playWhenReady = player.isPlaying();
            player.release();
            player = null;
        }
    }

}
