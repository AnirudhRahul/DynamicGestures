package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.AppConnection;
import org.weibeld.example.tabs.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/* Fragment used as page 3 */
public class Analytics extends Fragment {
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.analyticsfragment, container, false);
        TextView textView= (TextView) rootView.findViewById(R.id.reccomended);
        DataManager dataManager=new DataManager(getActivity().getApplicationContext());
        HashMap<String,HashMap<String,Integer>> appConnectionsMap=new HashMap<>();
        try {
            appConnectionsMap=dataManager.returnAppConnections();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<AppConnection> list=new ArrayList<>();
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
        textView.setText(list.toString().replaceAll(",","\n"));
        return rootView;
    }
}
