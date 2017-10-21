package org.weibeld.example.tabs.Fragments_and_UI;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.LauncherAdapter;
import org.weibeld.example.tabs.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LauncherSelector extends AppCompatActivity {
    LauncherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_selector);

        ListView listView=(ListView)findViewById(R.id.LauncherAppSelector);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appList = getAllInstalledApplications(getApplicationContext());

        ArrayList<App> apps=new ArrayList<>();
        int i=-1;
        for(ApplicationInfo e:appList){
            i++;

            //Package Name, Display Name, Icon Drawable, Index
            apps.add(new App(e.packageName,(String)e.loadLabel(pm),e.loadIcon(pm),i));
        }
        Collections.sort(apps);

        adapter=new LauncherAdapter(getApplicationContext(), apps);
        listView.setAdapter(adapter);

    }
    public static List<ApplicationInfo> getAllInstalledApplications(Context context) {
        List<ApplicationInfo> installedApps = context.getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>();
        for(int i =0; i<installedApps.size(); i++){
            if(context.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName) != null){
                //If you're here, then this is a launch-able app
                launchableInstalledApps.add(installedApps.get(i));
            }
        }
        return launchableInstalledApps;
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
