package com.yausername.youtubedl_android_example;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamingExampleActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartStream;

    private VideoView videoView;
    private ProgressBar pbLoading;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "StreamingExample";

    private WebView webView;

    private String matchedUrl; // Declare a class-level variable to store the matched URL


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_example);

        initViews();
        initListeners();

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
    }

    private void initViews() {
        btnStartStream = findViewById(R.id.btn_start_streaming);
        videoView = findViewById(R.id.video_view);
        pbLoading = findViewById(R.id.pb_status);
    }

    private void initListeners() {
        btnStartStream.setOnClickListener(this);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                videoView.start();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_streaming: {
                Log.d("Stringd", matchedUrl);
                startStream(matchedUrl);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void startStream(String url) {

        pbLoading.setVisibility(View.VISIBLE);
        Disposable disposable = Observable.fromCallable(() -> {
                    YoutubeDLRequest request = new YoutubeDLRequest(url);
                    // best stream containing video+audio
                    request.addOption("-f", "best");
                    return YoutubeDL.getInstance().getInfo(request);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(streamInfo -> {
                    pbLoading.setVisibility(View.GONE);
                    String videoUrl = streamInfo.getUrl();
                    if (TextUtils.isEmpty(videoUrl)) {
                        Toast.makeText(StreamingExampleActivity.this, "failed to get stream url", Toast.LENGTH_LONG).show();
                    } else {
                        setupVideoView(videoUrl);
                    }
                }, e -> {
                    if(BuildConfig.DEBUG) Log.e(TAG,  "failed to get stream info", e);
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(StreamingExampleActivity.this, "streaming failed. failed to get stream info", Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
    }

    private void setupVideoView(String videoUrl) {
        videoView.setVideoURI(Uri.parse(videoUrl));
    }
}
