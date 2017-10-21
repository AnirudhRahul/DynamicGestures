package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.AppConnection;
import org.weibeld.example.tabs.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* Fragment used as page 3 */
public class Analytics extends Fragment {
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    TextView textView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String name = preferences.getString("Launcher", "");

            if(name.length()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Please select your default launcher (Home app)\nThis is required to generate our Analytics");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "What is my Home App?",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    final Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                                    startActivity(intent);
                                }
                                else {
                                    final Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                                    startActivity(intent);
                                }

                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();}



        }
        else {
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = preferences.getString("Launcher", "");

        View rootView = inflater.inflate(R.layout.analyticsfragment, container, false);
        textView= (TextView) rootView.findViewById(R.id.reccomended);

        Button launcherButton=(Button)rootView.findViewById(R.id.selectLauncherBtn);
        launcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), LauncherSelector.class));
            }
        });
        ImageView imageView=(ImageView) rootView.findViewById(R.id.LauncherIcon);
        TextView launcherName=(TextView) rootView.findViewById(R.id.LauncherName);
        Log.v("he123",name);
        if(name.length()!=0) {
            PackageManager pm = getActivity().getPackageManager();
            List<ApplicationInfo> appList = getAllInstalledApplications(getActivity());
            for (ApplicationInfo e : appList) {
                if (e.packageName.equals(name)) {
                    launcherName.setText(e.loadLabel(pm));
                    imageView.setImageDrawable(e.loadIcon(pm));
                    break;
                }
            }
            new RetrieveMapTask().execute();
        }
        return rootView;
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


    private class RetrieveMapTask extends AsyncTask<Void,Void,Integer> {
        HashMap<String,HashMap<String,Integer>> appConnectionsMap=new HashMap<>();
        ArrayList<AppConnection> list=new ArrayList<>();
        @Override
        protected void onPostExecute(Integer result){
            Log.v("he123",list.toString());
            textView.setText(list.toString());
        }


        @Override
        protected Integer doInBackground(Void... params) {
            Log.v("he123","doInBackground");
            DataManager dataManager=new DataManager(getActivity().getApplicationContext());

            try {
                appConnectionsMap=dataManager.returnAppConnections();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for(String a:appConnectionsMap.keySet()){
                int total=0;
                for (String b:appConnectionsMap.get(a).keySet()){
                    total+=appConnectionsMap.get(a).get(b);
                }


                for(String b:appConnectionsMap.get(a).keySet()){
                    list.add(new AppConnection(a,b,appConnectionsMap.get(a).get(b),total));
                }
            }
            Collections.sort(list);
            return 1;
        }
    }





}
