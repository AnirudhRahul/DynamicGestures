package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.AppAdapter;
import org.weibeld.example.tabs.App;
import org.weibeld.example.tabs.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Fragment used as page 1 */
public class AppsFragment extends Fragment {
    AppAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appsfragment, container, false);
        Context context=getActivity().getApplicationContext();
        ListView listView=(ListView)rootView.findViewById(R.id.listview);
        DataManager dataManager=new DataManager(getActivity().getApplicationContext());
        PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> appList = getAllInstalledApplications(getActivity());

        ArrayList<App> apps=new ArrayList<>();
        int i=-1;
        for(ApplicationInfo e:appList){
            i++;

            //Package Name, Display Name, Icon Drawable, Index
            apps.add(new App(e.packageName,(String)e.loadLabel(pm),e.loadIcon(pm),i));
        }
        Collections.sort(apps);
        adapter=new AppAdapter(getActivity().getApplicationContext(), apps);
        listView.setAdapter(adapter);
        return rootView;

    }
    public void filterList(String a)
    {
        adapter.filter(a);
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
}
