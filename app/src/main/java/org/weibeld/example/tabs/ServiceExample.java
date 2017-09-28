package org.weibeld.example.tabs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 9/27/2017.
 */
public class ServiceExample extends Service {
    private int i = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            Timer timer  =  new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run()
                {
                    AppChecker appChecker = new AppChecker();
                    String packageName = appChecker.getForegroundApp(getApplicationContext());
                    if(packageName==null)
                        Log.v(TAG,"Null");
                    else
                        Log.v(TAG, packageName);
                }
            }, 2000, 6000);  // every 6 seconds
            return START_STICKY;
        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
