package org.weibeld.example.tabs;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.weibeld.example.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppSelectorSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_app_selector_settings);
        ListView listView=(ListView)findViewById(R.id.listview2);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        ArrayList<App> apps=new ArrayList<>();
        int i=-1;
        for(ResolveInfo e:pkgAppsList){
            i++;
            //Trust me it works ok
            apps.add(new App(e.loadLabel(getApplicationContext().getPackageManager()).toString(),e.loadIcon(getApplicationContext().getPackageManager()),i));
        }
        Collections.sort(apps);
        Intent intent=getIntent();
        int gestureIndex=intent.getIntExtra("GestureIndex",-1);
        int startingApp=intent.getIntExtra("StartingApp",-1);
        AppAdapterSettings adapter=new AppAdapterSettings(getApplicationContext(), apps,gestureIndex,startingApp);
        listView.setAdapter(adapter);




    }

}
