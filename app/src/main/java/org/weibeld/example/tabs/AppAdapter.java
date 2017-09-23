package org.weibeld.example.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.weibeld.example.R;

import java.util.ArrayList;

/**
 * Created by user on 9/22/2017.
 */

public class AppAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<App> apps;
    private LayoutInflater inflater;
    public AppAdapter(Context context, ArrayList<App> apps){
        this.context=context;
        this.apps=apps;
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
    public View getView(int i, View view, ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.applayout,parent,false);
        TextView appName=(TextView) rowView.findViewById(R.id.appName);
        ImageView appIcon=(ImageView) rowView.findViewById(R.id.appIcon);
        App app=(App) getItem(i);
        appName.setText(app.getName());
        appIcon.setImageDrawable(app.getIcon());
        return rowView;
    }
}
