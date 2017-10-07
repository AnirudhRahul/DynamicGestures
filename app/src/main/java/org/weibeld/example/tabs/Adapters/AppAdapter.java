package org.weibeld.example.tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.App;
import org.weibeld.example.tabs.Fragments_and_UI.SettingsActivity;

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
                i.putExtra("AppId",a);
                parent.getContext().startActivity(i);
            }
        });
        appName.setText(app.getName());
        appIcon.setImageDrawable(app.getIcon());
        return rowView;
    }
}
