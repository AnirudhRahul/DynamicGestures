package org.weibeld.example.tabs.Fragments_and_UI;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.AppAdapterSettings;
import org.weibeld.example.tabs.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppSelectorSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_app_selector_settings);

        ListView listView=(ListView)findViewById(R.id.listview2);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appList = pm.getInstalledApplications(0);

        ArrayList<App> apps=new ArrayList<>();
        int i=-1;
        for(ApplicationInfo e:appList){
            i++;
            if((e.flags&ApplicationInfo.FLAG_SYSTEM )!=0)
                continue;
            //Package Name, Display Name, Icon Drawable, Index
            apps.add(new App(e.packageName,(String)e.loadLabel(pm),e.loadIcon(pm),i));
        }
        Collections.sort(apps);

        Intent intent=getIntent();
        int gestureIndex=intent.getIntExtra("GestureIndex",-1);
        String startingAppPackageName=intent.getStringExtra("StartingApp");
        Log.v("gestures","Selector:"+apps.get(gestureIndex).getName());
        AppAdapterSettings adapter=new AppAdapterSettings(getApplicationContext(), apps,gestureIndex,startingAppPackageName);
        listView.setAdapter(adapter);




    }

}
