package com.example.myapp;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the key listener on the root view of the activity
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d("eventss", String.valueOf(event.getAction()));
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // Key down event
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    // Handle volume up key press
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    // Handle volume down key press
                    return true;
                // Add more key codes and corresponding actions as needed
            }
        }
        return false;
    }
}
