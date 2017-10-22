package org.weibeld.example.tabs;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.weibeld.example.tabs.Services.ServiceExample;

/**
 * Created by user on 9/28/2017.
 */

public class BroadCast extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private Context globContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        globContext=context;

        Intent i=new Intent(context, ServiceExample.class);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // DO WHATEVER YOU NEED TO DO HERE
            context.stopService(i);
            Log.v("tag","OFFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            context.startService(i);
            Log.v("tag","ONN");
        }


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) globContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}