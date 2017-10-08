package org.weibeld.example.tabs;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by user on 9/27/2017.
 */
public class ServiceExample extends Service  {
    private int i = 0;
    private boolean canceled=false;
    static final int MIN_DISTANCE = 150;
    private DataManager dataManager;
    private HashMap<String, HashMap<String,String>> masterList=new HashMap<>();
    private HashMap<String, HashMap<String,Boolean>> BooleanMasterList=new HashMap<>();
    private List<ApplicationInfo> appList;
    List<ResolveInfo> pkgAppsList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String curAppName="";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
        {

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> appList = pm.getInstalledApplications(0);
            HashMap<String,String> NametoPackageName=new HashMap<>();
            for(ApplicationInfo e:appList){
                NametoPackageName.put((String)e.loadLabel(pm),e.packageName);
                Log.v("AppListu",e.packageName+" "+e.loadLabel(pm));
            }

            dataManager=new DataManager(getApplicationContext());
            try{
            masterList=dataManager.returnMap();
            BooleanMasterList=dataManager.returnBooleanMap();}catch (Exception e){e.printStackTrace();}
            canceled=false;
            //Gesture Detection
            final WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

            Display display = mWindowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;
            int height = size.y;


            final LinearLayout LeftVerticalBar = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams VerticalBarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
           // verticalBar.setBackgroundColor(Color.parseColor("#000000"));
            LeftVerticalBar.setLayoutParams(VerticalBarParams);
            final WindowManager.LayoutParams WMparams = new WindowManager.LayoutParams(
                    50, /* width */
                    ViewGroup.LayoutParams.MATCH_PARENT, /* height */
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT
            );

            final LinearLayout ScreenLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams ScreenParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            ScreenLayout.setLayoutParams(ScreenParams);
            final WindowManager.LayoutParams ScreenWide = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, /* width */
                    ViewGroup.LayoutParams.MATCH_PARENT, /* height */
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT
            );
            WMparams.gravity = Gravity.LEFT | Gravity.TOP;
            ScreenWide.gravity=Gravity.LEFT | Gravity.TOP;
//            ScreenLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    switch(motionEvent.getAction())
//                    {
//                        case MotionEvent.ACTION_UP:
//                            float x=motionEvent.getX();
//                            Log.v("Movement",""+x);
//                            if (Math.abs(x) > MIN_DISTANCE)
//                            {
//                                Log.v("Movement","left to right swipe");
//                                Toaster("left to right swipe");
//                            }
//                            else
//                            {
//                                // consider as something else - a screen tap for example
//                            }
//                            Log.v("Movement","UPPPPPPPPPPPPPPPPPPPPPP");
//                            break;
//                    }
//
//                    return false;
//                }
//            });


            LeftVerticalBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ///Log.v("Movement",motionEvent.toString());
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        Log.v("Movement","Down");
                        mWindowManager.addView(ScreenLayout,ScreenWide);
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        float x=motionEvent.getX();
                        if(x>width/2.25){
                            Log.v("Movement","left to right swipe");

                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(masterList.get("Swipe Left").get(curAppName));
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                            }
                        }
                        mWindowManager.removeView(ScreenLayout);
                    }
                    return false;
                }
            });

           mWindowManager.addView(LeftVerticalBar, WMparams);
            WMparams.gravity = Gravity.RIGHT | Gravity.TOP;
            final LinearLayout RightVerticalBar = new LinearLayout(getApplicationContext());
            RightVerticalBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ///Log.v("Movement",motionEvent.toString());
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        Log.v("Movement","Down");
                        mWindowManager.addView(ScreenLayout,ScreenWide);
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        float x=motionEvent.getX();
                        Log.v("*XCOR",x+"");
                        if(-x>width/2.25){
                            Log.v("Movement","left to right swipe");

                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(masterList.get("Swipe Right").get(curAppName));
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                            }
                        }
                        mWindowManager.removeView(ScreenLayout);
                    }
                    return false;
                }
            });
            mWindowManager.addView(RightVerticalBar, WMparams);



//Keep track of current App
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
            }, 1000, 1000);  // every 6 seconds
            return START_STICKY;
        }


    @Override
    public void onCreate() {
        super.onCreate();

    }
//USE ACCESSIBILITY SERVICE


private void Toaster(final String x){
    Handler handler = new Handler(Looper.getMainLooper());

    handler.post(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(),x,Toast.LENGTH_LONG).show();
        }
    });
}
private int getAppIndex(String x){
    int i=0;
    for(ApplicationInfo e:appList){
        if(e.loadLabel(getApplication().getApplicationContext().getPackageManager()).toString().equals(x)){
            Log.v("Name",e.loadLabel(getApplication().getApplicationContext().getPackageManager()).toString());
           return i;
        }
        i++;
    }
    return i;
}
private String retrieveAppName(int x){
    return pkgAppsList.get(i).loadLabel(getApplication().getApplicationContext().getPackageManager()).toString();
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
        curAppName=currentApp;
        Log.v("Imp_ID", i+":"+"Current App in foreground is: " + currentApp);
        return currentApp;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        canceled=true;
        Log.v("destroy","DESTROYED");
    }

}
