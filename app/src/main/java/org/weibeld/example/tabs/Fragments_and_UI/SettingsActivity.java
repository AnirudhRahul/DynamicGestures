package org.weibeld.example.tabs.Fragments_and_UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    PackageManager pm;
    List<ApplicationInfo> appList;
    int index=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        pm = getApplicationContext().getPackageManager();
        appList = getAllInstalledApplications(getApplicationContext());
        DataManager dataManager=new DataManager(getApplicationContext());
        try{
        System.out.println(dataManager.returnMap().toString());}catch (Exception e){}
        //Set up the top activity bar
        ImageView appIconView=(ImageView) findViewById(R.id.appIcon);
        TextView appNameView=(TextView) findViewById(appName);
        Intent intent=getIntent();
        String packageName =intent.getStringExtra("AppId");
        index=getIndex(packageName);

//        String appName=getAppName(packageName);
//        Drawable drawable=getAppDrawable(packageName);
//
        appNameView.setText(appList.get(index).loadLabel(pm));
        appIconView.setImageDrawable(appList.get(index).loadIcon(pm));
        //Back Button setup

        //List View setup
        ListView settingsList=(ListView)findViewById(R.id.settingsList);
        ArrayList<String> gesture=new ArrayList<>();
        try{gesture=dataManager.returnGestureList();}catch (Exception e){e.printStackTrace();}
        Log.v("null",""+getApplicationContext());
        Collections.sort(gesture);
        Log.v("SettingStall","Gesture List retrieved and Sorted");
        SettingAdapter adapter=new SettingAdapter(getApplicationContext(),packageName,gesture);
        Log.v("Adapater",""+packageName);
        Log.v("Adapater",""+gesture);

        settingsList.setAdapter(adapter);
        Log.v("SettingStall","Gesture List Pumped");



    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.animator.fade_animation_out,R.animator.fade_animation_in);
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }
    public static List<ApplicationInfo> getAllInstalledApplications(Context context) {
        List<ApplicationInfo> installedApps = context.getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>(installedApps.size());
        for(int i =0; i<installedApps.size(); i++){
            if(context.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName) != null){
                //If you're here, then this is a launch-able app
                launchableInstalledApps.add(installedApps.get(i));
            }
        }
        return launchableInstalledApps;
    }
public int getIndex(String packageName){
    int i=0;
    for(ApplicationInfo e:appList){

        if(packageName.equals(e.packageName)){
            return i;
        }
        i++;
    }
    return -1;
}

    public Drawable getAppDrawable(String packageName){

        for(ApplicationInfo e:appList){

            if(packageName.equals(e.packageName)){
                return e.loadIcon(pm);
            }
        }
        return null;
    }
    public String getAppName(String packageName){
        for(ApplicationInfo e:appList){

            if(packageName.equals(e.packageName)){
                return (String)e.loadLabel(pm);
            }
        }
        return null;
    }





}
