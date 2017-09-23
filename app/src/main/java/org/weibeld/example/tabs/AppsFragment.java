package org.weibeld.example.tabs;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.weibeld.example.R;

import java.util.ArrayList;
import java.util.List;

/* Fragment used as page 1 */
public class AppsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appsfragment, container, false);
        ListView listView=(ListView)rootView.findViewById(R.id.listview);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getActivity().getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        ArrayList<App> apps=new ArrayList<>();
        for(ResolveInfo e:pkgAppsList){
            //Trust me it works ok
            apps.add(new App(e.loadLabel(getActivity().getApplicationContext().getPackageManager()).toString(),e.loadIcon(getActivity().getApplicationContext().getPackageManager())));
        }
        AppAdapter adapter=new AppAdapter(getActivity().getApplicationContext(), apps);
        listView.setAdapter(adapter);
        return rootView;

    }
}
