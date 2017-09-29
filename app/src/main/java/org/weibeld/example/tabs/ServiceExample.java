package org.weibeld.example.tabs;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by user on 9/27/2017.
 */
public class ServiceExample extends Service {
    private int i = 0;
    private boolean canceled=false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            canceled=false;
            final Timer timer  =  new Timer();
            int i=0;
            timer.purge();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run()
                {
                if(canceled)
                    timer.cancel();

               printForegroundTask();
                }
            }, 2000, 6000);  // every 6 seconds
            return START_STICKY;
        }

    private String printForegroundTask() {
        i++;
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)this.getSystemService(getApplicationContext().USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.v("adapter", i+":"+"Current App in foreground is: " + currentApp);
        return currentApp;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        canceled=true;
        Log.v("destroy","DESTROYED");
    }
}
