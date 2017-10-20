package org.weibeld.example.tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.DataManager;
import org.weibeld.example.tabs.GestureSettings;
import org.weibeld.example.tabs.Services.LeftBarService;
import org.weibeld.example.tabs.Services.RightBarService;
import org.weibeld.example.tabs.Services.ServiceExample;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user on 10/2/2017.
 */

public class GestureAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> finalgestures;
    private ArrayList<String> gesturesInUse;
    private LayoutInflater inflater;
    private DataManager dataManager;
    public GestureAdapter(Context context, ArrayList<String> finalgestures){
        this.context=context;
        this.finalgestures=finalgestures;
        dataManager=new DataManager(context);
        Collections.sort(finalgestures);
        try{gesturesInUse=dataManager.returnGestureList();}catch (Exception e){e.printStackTrace();}
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return finalgestures.size();
    }

    @Override
    public Object getItem(int i) {
        return finalgestures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void  updateService(){
        Log.v("gestureInUse",gesturesInUse.toString());
        context.stopService(new Intent(context,ServiceExample.class));
        context.stopService(new Intent(context,LeftBarService.class));
        context.stopService(new Intent(context,RightBarService.class));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        if(gesturesInUse.size()==2) {
            context.startService(new Intent(context, ServiceExample.class));
            editor.putString("Service","Both");
        }
        else if(gesturesInUse.size()==0){
            editor.putString("Service","None");
        }
        else if(gesturesInUse.get(0).equals("Left Swipe")) {
           // Log.v("gestureInUse","Left Swipe:Reached");
            editor.putString("Service","Left");
            context.startService(new Intent(context, LeftBarService.class));
        }
        else if(gesturesInUse.get(0).equals("Right Swipe")) {
           // Log.v("gestureInUse","Right Swipe:Reached");
            editor.putString("Service","Right");
            context.startService(new Intent(context, RightBarService.class));
        }
        editor.apply();


    }
    @Override
    public View getView(final int i, View view, final ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.gesture_row,parent,false);
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent temp=new Intent(context, GestureSettings.class);
                temp.putExtra("GestureName",finalgestures.get(i));

                context.startActivity(temp);
                return false;
            }
        });

        TextView gestureName=(TextView)rowView.findViewById(R.id.gestureName_Checkbox);
        gestureName.setText(finalgestures.get(i));
        final CheckBox checkBox=(CheckBox)rowView.findViewById(R.id.gesture_Checkbox);
        if(gesturesInUse.size()>0&&gesturesInUse.contains(finalgestures.get(i))){
            checkBox.setChecked(true);
        }
        final int a=i;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Intent i=new Intent(context, ServiceExample.class);
                if ( isChecked ) {
                    gesturesInUse.add(finalgestures.get(a));
                }
                else{
                    gesturesInUse.remove(finalgestures.get(a));
                }
                try {
                    dataManager.WriteGesturesList(gesturesInUse);
                    dataManager.UpdateBooleanMap();
                    dataManager.UpdateMap();
                    Log.v("impertant","Reached");
                }catch (Exception e){e.printStackTrace();}
                updateService();
            }
        });

        return rowView;
    }
}

