package com.nitesh.bt;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((AudioManager)getSystemService(AUDIO_SERVICE))
                .registerMediaButtonEventReceiver(new ComponentName(this, ButtonReceiver.class));

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
