package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.AppOpsManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.BroadCast;
import org.weibeld.example.tabs.DataManager;
import org.weibeld.example.tabs.ServiceExample;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {"Apps", "Gestures", "Analytics"};

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {new AppsFragment(), new GesturesFragment(), new Analytics()};
    private BroadcastReceiver mReceiver = new BroadCast();
    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager myViewPager;
    //HashMap<GestureName, HashMap<InitialAppIndex,FinalAppIndex>>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataManager dataManager=new DataManager(getApplicationContext());
        try {
            dataManager.UpdateMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }else{
        }






        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, ServiceExample.class));
        getSupportActionBar().setElevation(0);
       // getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#354777\">" + getString(R.string.app_name) + "</font>"));

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new BroadCast();
        registerReceiver(mReceiver, filter);

        // Connect the ViewPager to our custom PagerAdapter. The PagerAdapter supplies the pages
        // (fragments) to the ViewPager, which the ViewPager needs to display.
        myViewPager = (ViewPager) findViewById(R.id.viewpager);
        myViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(myViewPager);




    }
    @Override
    protected void onStop()
    {
        if(mReceiver.isOrderedBroadcast())
        unregisterReceiver(mReceiver);
        super.onStop();
    }
    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menuSearch);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AppsFragment fragment =  (AppsFragment)getFragmentManager().findFragmentByTag(makeFragmentName(R.id.viewpager, 0));
                fragment.filterList(newText);
                Log.v("KeyInput",newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



}
