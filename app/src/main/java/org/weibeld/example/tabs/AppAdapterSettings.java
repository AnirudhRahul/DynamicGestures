package org.weibeld.example.tabs;

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

import java.io.IOException;
import java.util.ArrayList;

public class AppAdapterSettings extends BaseAdapter {
    private Context context;
    private ArrayList<App> apps;
    private LayoutInflater inflater;
    private int gestureIndex;
    private int starterApp;
    public AppAdapterSettings(Context context, ArrayList<App> apps,int gestureIndex, int starterApp){
        this.context=context;
        this.apps=apps;
        this.gestureIndex=gestureIndex;
        this.starterApp=starterApp;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.applayout,parent,false);
        TextView appName=(TextView) rowView.findViewById(R.id.appName);
        ImageView appIcon=(ImageView) rowView.findViewById(R.id.appIcon);
        final Button appBtn=(Button) rowView.findViewById(R.id.appBtn);
        App app=(App) getItem(i);
        final int a=app.getIndex();
        appBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(parent.getContext(), SettingsActivity.class);
                i.putExtra("AppId",starterApp);

                DataManager dataManager=new DataManager(parent.getContext());
                ArrayList<String> gestures=new ArrayList<>();
                try{
                    gestures.addAll(dataManager.returnGestureList());
                    String gestureName=gestures.get(gestureIndex);
                    Log.v("IMPORTANT",gestureName);
                    dataManager.UpdateMap();
                    dataManager.AddConnection(gestureName, starterApp, a);
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
