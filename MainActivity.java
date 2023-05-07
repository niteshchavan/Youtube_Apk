package com.nitesh.bt;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a PendingIntent for the MediaButtonReceiver
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(new ComponentName(this, ButtonReceiver.class));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);

        // Create a MediaSessionCompat instance
        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "yourMediaSessionTag");
        mediaSession.setMediaButtonReceiver(pendingIntent);
        mediaSession.setActive(true);
    }

    public static class ButtonReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            Log.d("sjdfsjh", intentAction);
            Toast.makeText(context, "debug media button test", Toast.LENGTH_SHORT).show();
        }
    }
}
