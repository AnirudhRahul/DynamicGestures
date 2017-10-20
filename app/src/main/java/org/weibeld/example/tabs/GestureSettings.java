package org.weibeld.example.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.weibeld.example.R;


public class GestureSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_settings);
        Intent intent=getIntent();
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(intent.getStringExtra("GestureName"));
    }
}
