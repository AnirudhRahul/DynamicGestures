package org.weibeld.example.tabs.Fragments_and_UI;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.AppAdapterSettings;
import org.weibeld.example.tabs.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LauncherSelector extends AppCompatActivity {
    AppAdapterSettings adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_selector);

        ListView listView=(ListView)findViewById(R.id.listviewSecondaryAppSelector);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appList = pm.getInstalledApplications(0);

        ArrayList<App> apps=new ArrayList<>();
        int i=-1;
        for(ApplicationInfo e:appList){
            i++;

            //Package Name, Display Name, Icon Drawable, Index
            apps.add(new App(e.packageName,(String)e.loadLabel(pm),e.loadIcon(pm),i));
        }
        Collections.sort(apps);

        Intent intent=getIntent();
        int gestureIndex=intent.getIntExtra("GestureIndex",-1);
        String startingAppPackageName=intent.getStringExtra("StartingApp");
        Log.v("gestures","Selector:"+apps.get(gestureIndex).getName());
        adapter=new AppAdapterSettings(getApplicationContext(), apps,gestureIndex,startingAppPackageName);
        listView.setAdapter(adapter);

    }



    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menuSearch);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
//                Log.v("KeyInput",newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }





}
}
