package com.yausername.youtubedl_android_example;


import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StreamingExampleActivity extends AppCompatActivity {

    private static final String TAG = "StreamingExample";
    private ExoPlayer player;
    private WebView webView;
    private String matchedUrl;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_example);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (url.contains("youtube.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("i.ytimg.com")) {
                    String regex = ".*\\/vi\\/(.*?)\\/.*";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(url);
                    if (matcher.matches()) {
                        String videoId = matcher.group(1);
                        matchedUrl = "https://youtu.be/" + videoId;
                        Log.d("matched", matchedUrl);


                    }
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("accounts.google.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("www.gstatic.com")) {
                    return super.shouldInterceptRequest(view, request);
                }
                return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
            }

        });
        webView.loadUrl("https://m.youtube.com/");
        Button playButton = findViewById(R.id.btn_start_streaming);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStream(matchedUrl);
            }
        });


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



    private void releasePlayer() {
        if (player != null) {
            long playbackPosition = player.getCurrentPosition();
            boolean playWhenReady = player.isPlaying();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
    private void startStream(String url) {


        Disposable disposable = Observable.fromCallable(() -> {
                    YoutubeDLRequest request = new YoutubeDLRequest(url);
                    // best stream containing video+audio
                    request.addOption("-f", "best");
                    return YoutubeDL.getInstance().getInfo(request);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(streamInfo -> {

                    String videoUrl = streamInfo.getUrl();
                    if (TextUtils.isEmpty(videoUrl)) {
                        Toast.makeText(StreamingExampleActivity.this, "failed to get stream url", Toast.LENGTH_LONG).show();
                    } else {
                        setupVideoView(videoUrl);
                    }
                }, e -> {
                    if(BuildConfig.DEBUG) Log.e(TAG,  "failed to get stream info", e);

                    Toast.makeText(StreamingExampleActivity.this, "streaming failed. failed to get stream info", Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
    }
    private void setupVideoView(String videoUrl) {
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
}
