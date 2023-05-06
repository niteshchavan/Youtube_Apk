package com.yausername.youtubedl_android_example;


import android.content.Context;
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
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLRequest;

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

    private PlayerView playerView;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_example);
        //btn_start_streaming = findViewById(R.id.btn_start_streaming);
        playerView = findViewById(R.id.player_view);
        webView = (WebView) findViewById(R.id.webview);

        player = new ExoPlayer.Builder(this).build();
        playerView.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            private String previousUrl = "";

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String head = request.getRequestHeaders().toString();
                String regex = "\\bhttps?://m\\.youtube\\.com/watch\\?v=[a-zA-Z0-9_-]{11}\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(head);

                if (matcher.find()) {
                    matchedUrl = matcher.group();

                    if (!matchedUrl.equals(previousUrl)) {
                        // Process the new matchedUrl
                        startStream(matchedUrl);
                        previousUrl = matchedUrl;
                        Log.d("previousUrl", matchedUrl);
                    }
                }
                String url = request.getUrl().toString();

                if (url.contains("youtube.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("i.ytimg.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("accounts.google.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("www.gstatic.com")) {
                    return super.shouldInterceptRequest(view, request);
                } else if (url.contains("ggpht.com")) {
                    return super.shouldInterceptRequest(view, request);
                }

                return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));

            }

        });
        webView.loadUrl("https://m.youtube.com/");


        //Button playButton = findViewById(R.id.btn_start_streaming);
        //playButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //playerView.setVisibility(View.VISIBLE);
        //        startStream(matchedUrl);
        //    }
        //});


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            // Go back in the web view history
            webView.goBack();
            playerView.setVisibility(View.GONE);
        } else if (playerView.getVisibility() == View.GONE) {
            playerView.setVisibility(View.VISIBLE);
        } else if (playerView.getVisibility() == View.VISIBLE) {
            playerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            webView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            //playerView.setLayoutParams(layoutParams);
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.topMargin = convertDpToPixel(0, this);
                playerView.setLayoutParams(marginLayoutParams);
            }
            hideSystemUI();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            webView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = convertDpToPixel(200, this); // Set an appropriate height for portrait mode
            // Set the top margin to 50
            //playerView.setLayoutParams(layoutParams);
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.topMargin = convertDpToPixel(50, this);
                playerView.setLayoutParams(marginLayoutParams);
            }
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

    private int convertDpToPixel(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }


    private void releasePlayer() {
        if (player != null) {
            player.stop();  // Stop the playback
            player.release();  // Release the player resources
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        releasePlayer();
        super.onDestroy();
    }
    private void startStream(String url) {


        Disposable disposable = Observable.fromCallable(() -> {
                    YoutubeDLRequest request = new YoutubeDLRequest(url);
                    // best stream containing video+audio
                    request.addOption("-f", "18");
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
        playerView.setVisibility(View.VISIBLE);
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);

        player.setMediaItem(mediaItem);
        // Prepare the player
        player.prepare();
        // Start the playback
        player.play();

    }
}
