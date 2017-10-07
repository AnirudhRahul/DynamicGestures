package org.weibeld.example.tabs.Adapters;

import android.content.Context;
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

    @Override
    public View getView(int i, View view, final ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.gesture_row,parent,false);
        TextView gestureName=(TextView)rowView.findViewById(R.id.gestureName_Checkbox);
        gestureName.setText(finalgestures.get(i));
        CheckBox checkBox=(CheckBox)rowView.findViewById(R.id.gesture_Checkbox);
        if(gesturesInUse.size()>0&&gesturesInUse.contains(finalgestures.get(i))){
            checkBox.setChecked(true);
        }
        final int a=i;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
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

            }
        });

        return rowView;
    }
}

