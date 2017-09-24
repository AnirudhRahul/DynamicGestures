package org.weibeld.example.tabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.weibeld.example.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.settings_title_bar);
    }

}
