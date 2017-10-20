package org.weibeld.example.tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.App;
import org.weibeld.example.tabs.DataManager;
import org.weibeld.example.tabs.Fragments_and_UI.SettingsActivity;

import java.io.IOException;
import java.util.ArrayList;

public class AppAdapterSettings extends BaseAdapter {
    private Context context;
    private ArrayList<App> allApps=new ArrayList<>();
    private ArrayList<App> displayedApps=new ArrayList<>();
    private LayoutInflater inflater;
    private int gestureIndex;
    private String starterAppPackageName;
    public AppAdapterSettings(Context context, ArrayList<App> apps,int gestureIndex, String starterAppPackageName){
        this.context=context;
        this.allApps.addAll(apps);
        displayedApps.addAll(apps);
        this.gestureIndex=gestureIndex;
        this.starterAppPackageName=starterAppPackageName;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return displayedApps.size();
    }

    @Override
    public Object getItem(int i) {
        return displayedApps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void filter(String a){
        displayedApps.clear();
        if(a.equals("")) {
            displayedApps.addAll(allApps);
            return;
        }
        a=a.toLowerCase();
        for(int i=0;i<allApps.size();i++){
            if(allApps.get(i).getName().toLowerCase().indexOf(a)>=0)
                displayedApps.add(allApps.get(i));
        }
        Log.v("",displayedApps.toString());
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, final ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.applayout,parent,false);
        TextView appName=(TextView) rowView.findViewById(R.id.appName);
        ImageView appIcon=(ImageView) rowView.findViewById(R.id.appIcon);
        final Button appBtn=(Button) rowView.findViewById(R.id.appBtn);
        App app=(App) getItem(i);
        final String a=app.getPackagename();
        appBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(parent.getContext(), SettingsActivity.class);
                i.putExtra("AppId",starterAppPackageName);

                DataManager dataManager=new DataManager(parent.getContext());
                ArrayList<String> gestures=new ArrayList<>();
                try{
                    gestures.addAll(dataManager.returnGestureList());
                    String gestureName=gestures.get(gestureIndex);
                    Log.v("IMPORTANT",gestureName);
                    dataManager.AddConnection(gestureName, starterAppPackageName, a);
                    Log.v("IMPORTANT",dataManager.returnMap().toString());

                }catch (ClassNotFoundException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}

                parent.getContext().startActivity(i);
            }
        });
        appName.setText(app.getName());
        appIcon.setImageDrawable(app.getIcon());
        return rowView;
    }
}
