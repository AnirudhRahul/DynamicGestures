package org.weibeld.example.tabs;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.weibeld.example.R;

import java.util.List;

import static org.weibeld.example.R.id.appName;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton backButton=(ImageButton) findViewById(R.id.backbtn);
        ImageView appIconView=(ImageView) findViewById(R.id.appIcon);
        TextView appNameView=(TextView) findViewById(appName);
        Intent intent=getIntent();
        int index =intent.getIntExtra("AppId",-1);
        String appName="";
        Drawable drawable=null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        ResolveInfo AppInfo=packages.get(index);
        appName=AppInfo.loadLabel(getApplicationContext().getPackageManager()).toString();
        drawable=AppInfo.loadIcon(getApplicationContext().getPackageManager());
        appNameView.setText(appName);
        appIconView.setImageDrawable(drawable);




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

}
