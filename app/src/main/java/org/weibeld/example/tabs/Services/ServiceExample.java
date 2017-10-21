package org.weibeld.example.tabs.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.weibeld.example.tabs.DataManager;

import java.io.IOException;
import java.util.ArrayList;
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
    private String launcher="";
    private HashMap<String, HashMap<String,String>> masterList=new HashMap<>();
    private HashMap<String, HashMap<String,Boolean>> BooleanMasterList=new HashMap<>();
    private ArrayList<String> usedAppList=new ArrayList<>();
    private List<ApplicationInfo> appList;
    List<ResolveInfo> pkgAppsList;
    int[] alphaValues={32,64,80,96,112,128,192,0};
    private ArrayList<Integer> alpha=new ArrayList<>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public int dpTopx(int dp){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return (int)((dp * displayMetrics.density) + 0.5);
    }
    public String formatAlpha(int x){
        Log.v("Alpha",String.format("%02X", x));
       return String.format("%02X", x);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            launcher=preferences.getString("Launcher","");

            if(alpha.size()==0)
            for(int e:alphaValues)
                alpha.add(e);
            PackageManager pm = getPackageManager();
            List<ApplicationInfo> appList = getAllInstalledApplications(getApplicationContext());
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
            final int widthOfBar=(int)(getScreenWidth()*0.04);
            final int heightOfBar=(int)(getScreenHeight()*0.23);
            Display display = mWindowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;
            final int height = size.y;

            final LinearLayout LeftVerticalBar = new LinearLayout(getApplicationContext());
            LeftVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));
            final WindowManager.LayoutParams WMparams = new WindowManager.LayoutParams(
                    widthOfBar, /* width */
                    heightOfBar, /* height */
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT
            );

            //final LinearLayout ScreenLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams ScreenParams = new LinearLayout.LayoutParams(0,0);
           // ScreenLayout.setLayoutParams(ScreenParams);
            final WindowManager.LayoutParams ScreenWide = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, /* width */
                    ViewGroup.LayoutParams.MATCH_PARENT, /* height */
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT
            );
            WMparams.gravity = Gravity.LEFT;
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

            Log.v("Init MARGIN",""+WMparams.verticalMargin );
            final LinearLayout RightVerticalBar = new LinearLayout(getApplicationContext());
            RightVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));

            LeftVerticalBar.setOnTouchListener(new View.OnTouchListener() {
                float y1=0;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ///Log.v("Movement",motionEvent.toString());
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                      //  mWindowManager.addView(ScreenLayout,ScreenWide);
                        y1=motionEvent.getY();
                        Log.v("Y1U",""+y1);
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {

                            float y2 = motionEvent.getRawY()+heightOfBar/2-y1;
                            float newMargin=(float) (y2 * (1.0) / getScreenHeight() - 0.5);
                          //  if(Math.abs(WMparams.verticalMargin-newMargin)>0.01)
                            WMparams.verticalMargin = newMargin;

                            WMparams.gravity = Gravity.LEFT;
                            mWindowManager.updateViewLayout(LeftVerticalBar, WMparams);

                            WMparams.gravity = Gravity.RIGHT;
                            mWindowManager.updateViewLayout(RightVerticalBar, WMparams);

                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        float x=motionEvent.getX();
                        Log.v("DeltaY",""+y1+":"+motionEvent.getY());
                        if(x>width/2.25){
                            Log.v("Movement","left to right swipe");
                            String nextAppPackageName=masterList.get("Left Swipe").get(usedAppList.get(usedAppList.size()-1));
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(nextAppPackageName);
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                if(!nextAppPackageName.equals(usedAppList.get(usedAppList.size()-1)))
                                    usedAppList.add(nextAppPackageName);
//                                try {
//                                    short t1=PackageToShort(prevAppName);
//                                    short t2=PackageToShort(curAppName);
//                                    if(t1!=-1&&t2!=-1)
//                                        dataManager.addAppConnection(PackageToShort(prevAppName),PackageToShort(curAppName));
//                                    dataManager.logAppConnections();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }

                            }
                        }
                        else if(x<widthOfBar&&Math.abs(y1-motionEvent.getY())<10){
                            alpha.add(alpha.remove(0));
                            LeftVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));
                            RightVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));
                        }
                      //  mWindowManager.removeView(ScreenLayout);
                    }

                    return false;
                }
            });
            WMparams.verticalMargin=(float)0;

                mWindowManager.addView(LeftVerticalBar, WMparams);

            WMparams.gravity = Gravity.RIGHT ;
            RightVerticalBar.setOnTouchListener(new View.OnTouchListener() {
                float y1=0;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ///Log.v("Movement",motionEvent.toString());
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        y1=motionEvent.getY();
                        Log.v("Movement","Down");
                    //    mWindowManager.addView(ScreenLayout,ScreenWide);
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                        float y2 = motionEvent.getRawY()+heightOfBar/2-y1;
                        float newMargin=(float) (y2 * (1.0) / getScreenHeight() - 0.5);
                       if(Math.abs(WMparams.verticalMargin-newMargin)>0.01)
                            WMparams.verticalMargin = newMargin;

                        WMparams.gravity = Gravity.LEFT;
                            mWindowManager.updateViewLayout(LeftVerticalBar,WMparams);

                        WMparams.gravity = Gravity.RIGHT;
                            mWindowManager.updateViewLayout(RightVerticalBar,WMparams);
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        float x=motionEvent.getX();
                        Log.v("*XCOR",x+"");
                        Log.v("*XCOR",widthOfBar+"");
                        Log.v("DeltaY",""+Math.abs(y1-motionEvent.getRawY()));
                        if(-x>width/2.25){
                            Log.v("Movement","left to right swipe");
                            Toaster("Swipe");
                            String nextAppPackageName=masterList.get("Right Swipe").get(usedAppList.get(usedAppList.size()-1));
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(nextAppPackageName);
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                if(!nextAppPackageName.equals(usedAppList.get(usedAppList.size()-1)))
                               usedAppList.add(nextAppPackageName);


//                                try {
//                                    short t1=PackageToShort(prevAppName);
//                                    short t2=PackageToShort(curAppName);
//                                    if(t1!=-1&&t2!=-1)
//                                        dataManager.addAppConnection(PackageToShort(prevAppName),PackageToShort(curAppName));
//                                    dataManager.logAppConnections();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }
                            }

                        }else if(x<widthOfBar&&Math.abs(y1-motionEvent.getY())<10){
                            alpha.add(alpha.remove(0));
                            Log.v("*XCOR",x+"TRIGGERED");
                            LeftVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));
                            RightVerticalBar.setBackgroundColor(Color.parseColor("#"+formatAlpha(alpha.get(0))+"000000"));

                        }
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
                if(canceled) {
                    timer.cancel();
                    mWindowManager.removeView(RightVerticalBar);
                    mWindowManager.removeView(LeftVerticalBar);
                }
                 printForegroundTask();
//                Log.v("AppStack","PrevAppName: "+prevAppName+"\nCurApp: "+curAppName);
                }
            }, 100, 100);  // every 6 seconds
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
        if(usedAppList.size()==0)
            usedAppList.add(currentApp);
        else if(!currentApp.equals(usedAppList.get(usedAppList.size()-1))){
            usedAppList.add(currentApp);
            try {
                updateAppUsageMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Log.v("Imp_ID", i+":"+"Current App in foreground is: " + usedAppList);
        return currentApp;
    }

    public short PackageToShort(String a){
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> appList = getAllInstalledApplications(getApplicationContext());
        short counter=0;
        for(ApplicationInfo e:appList){
            //Package Name, Display Name, Icon Drawable, Index
            if(e.packageName.equals(a))
                return counter;
            counter++;
        }
        Log.v("he123","wat:"+a);
        return -1;
    }
    public void updateAppUsageMap() throws IOException, ClassNotFoundException {
        if(launcher.length()==0)
            return;
        if(usedAppList.size()<3)
            return;
        while (usedAppList.size()>3)
            usedAppList.remove(0);
        boolean[] launcherArray=new boolean[3];
        for(int i=0;i<3;i++){
            launcherArray[i]=usedAppList.get(i).equals(launcher);
        }
        if(!launcherArray[0]&&launcherArray[1]&&!launcherArray[2]&&!usedAppList.get(0).equals(usedAppList.get(2)))
            dataManager.addAppConnection(PackageToShort(usedAppList.get(0)),PackageToShort(usedAppList.get(2)));
        dataManager.logAppConnections();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        canceled=true;
        Log.v("destroy","DESTROYED");
    }

}
