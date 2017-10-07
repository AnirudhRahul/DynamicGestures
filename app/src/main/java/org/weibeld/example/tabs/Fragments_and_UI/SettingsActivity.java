package org.weibeld.example.tabs.Fragments_and_UI;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.SettingAdapter;
import org.weibeld.example.tabs.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.weibeld.example.R.id.appName;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DataManager dataManager=new DataManager(getApplicationContext());
        try{
        System.out.println(dataManager.returnMap().toString());}catch (Exception e){}
        //Set up the top activity bar
        ImageButton backButton=(ImageButton) findViewById(R.id.backbtn);
        ImageView appIconView=(ImageView) findViewById(R.id.appIcon);
        TextView appNameView=(TextView) findViewById(appName);
        Intent intent=getIntent();
        String packageName =intent.getStringExtra("AppId");

        String appName=getAppName(packageName);
        Drawable drawable=getAppDrawable(packageName);

        appNameView.setText(appName);
        appIconView.setImageDrawable(drawable);
        //Back Button setup
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        //List View setup
        ListView settingsList=(ListView)findViewById(R.id.settingsList);
        ArrayList<String> gesture=new ArrayList<>();
        try{gesture=dataManager.returnGestureList();}catch (Exception e){e.printStackTrace();}
        Log.v("null",""+getApplicationContext());

        Collections.sort(gesture);
        SettingAdapter adapter=new SettingAdapter(getApplicationContext(),packageName,gesture);
        Log.v("Adapater",""+packageName);
        Log.v("Adapater",""+gesture);

        settingsList.setAdapter(adapter);



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    public Drawable getAppDrawable(String packageName){
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> appList = pm.getInstalledApplications(0);
        for(ApplicationInfo e:appList){
            if((e.flags&ApplicationInfo.FLAG_SYSTEM )!=0)
                continue;
            if(packageName.equals(e.packageName)){
                return e.loadIcon(pm);
            }
        }
        return null;
    }
    public String getAppName(String packageName){
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> appList = pm.getInstalledApplications(0);
        for(ApplicationInfo e:appList){
            if((e.flags&ApplicationInfo.FLAG_SYSTEM )!=0)
                continue;
            if(packageName.equals(e.packageName)){
                return (String)e.loadLabel(pm);
            }
        }
        return null;
    }





}
