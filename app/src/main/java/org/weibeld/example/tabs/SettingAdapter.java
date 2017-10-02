package org.weibeld.example.tabs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.weibeld.example.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.weibeld.example.R.id.selectAppIcon;

/**
 * Created by user on 9/29/2017.
 */

public class SettingAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    DataManager dataManager;
    private ArrayList<String> gestures=new ArrayList<>();
    private HashMap<String,HashMap<Integer,Integer>> masterList=new HashMap<>();
    private HashMap<String,HashMap<Integer,Boolean>> BooleanMasterList=new HashMap<>();
    private int initialApp;
    public SettingAdapter(Context context, int initialApp, ArrayList<String> gestures, HashMap<String,HashMap<Integer,Integer>> masterList){
        dataManager=new DataManager(context);
        this.context=context;
        this.gestures=gestures;
        this.initialApp=initialApp;
        try{BooleanMasterList=dataManager.returnBooleanMap();}catch (Exception e){e.printStackTrace();}
        this.masterList=masterList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return gestures.size();
    }

    @Override
    public Object getItem(int i) {
        return gestures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
//    public void UpdateSwitchStates(){
//        SwitchStates.clear();
//        for(String e:gestures) {
//            SwitchStates.add(BooleanMasterList.containsKey(e)&&BooleanMasterList.get(e).containsKey(initialApp)&&BooleanMasterList.get(e).get(initialApp));
//        }
//    }
    @Override
    public View getView(int i, View view, final ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.setting_row,parent,false);
        TextView Gesture=(TextView)rowView.findViewById(R.id.gestureName);
        Gesture.setText(gestures.get(i));
        Button selectAppBtn=(Button)rowView.findViewById(R.id.selectAppBtn);

        final RelativeLayout AppSelector=(RelativeLayout)rowView.findViewById(R.id.appSelectorSection);
        Switch UsingGesture=(Switch)rowView.findViewById(R.id.gestureUsed);
        UsingGesture.setChecked(BooleanMasterList.get(gestures.get(i)).containsKey(initialApp)&&BooleanMasterList.get(gestures.get(i)).get(initialApp));
        if(!UsingGesture.isChecked())
            collapse(AppSelector,1);

//        Log.v("mapu",""+SwitchStates.get(i));
        final TextView selectedAppName=(TextView)rowView.findViewById(R.id.selectAppName);
        final ImageView selectedAppIcon=(ImageView)rowView.findViewById(selectAppIcon);

        final int a=i;
        UsingGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                collapse(AppSelector,1);
                Log.v("HEIGHTU",""+AppSelector.getHeight());
                try{dataManager.AddToBooleanMap(gestures.get(a),initialApp,isChecked); Log.v("mapu",dataManager.returnBooleanMap().toString());}catch(Exception e){e.printStackTrace();}

                int index=-1;
                try{
                    index=masterList.get(gestures.get(a)).get(initialApp);}catch (Exception e){}
                if(index!=-1&&isChecked) {
                    setRow(index, selectedAppIcon, selectedAppName);
                    //    Doublecollapse(AppSelector,1);
                }


            }
        });
        selectAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(parent.getContext(), AppSelectorSettings.class);
                i.putExtra("GestureIndex",a);
                i.putExtra("StartingApp",initialApp);
                parent.getContext().startActivity(i);
            }
        });


        int index=-1;
        try{
        index=masterList.get(gestures.get(i)).get(initialApp);}catch (Exception e){}
        if(index!=-1&&UsingGesture.isChecked()) {
            setRow(index, selectedAppIcon, selectedAppName);
            Doublecollapse(AppSelector,1);
        }




        return rowView;
    }
    public void setRow(int index,ImageView AppIcon, TextView AppName){
        AppIcon.setImageDrawable(getAppDrawable(index));
        AppName.setText(getAppName(index));
    }

    public Drawable getAppDrawable(int index){
        Drawable drawable=null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        ResolveInfo AppInfo=packages.get(index);
        drawable=AppInfo.loadIcon(context.getPackageManager());
        return drawable;
    }
    public String getAppName(int index){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        ResolveInfo AppInfo=packages.get(index);
        return AppInfo.loadLabel(context.getPackageManager()).toString();
    }

    public static void collapse(final View v, int duration) {
        final boolean expand = v.getVisibility()!=View.VISIBLE;

        int prevHeight  = v.getHeight();
        int height = 0;
        if (expand) {
            int measureSpecParams = View.MeasureSpec.getSize(View.MeasureSpec.UNSPECIFIED);
            v.measure(measureSpecParams, measureSpecParams);
            height = v.getMeasuredHeight();
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (expand){
                    v.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expand){
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
    public static void Doublecollapse(final View v, final int duration) {
        final boolean expand = v.getVisibility()!=View.VISIBLE;

        int prevHeight  = v.getHeight();
        int height = 0;
        if (expand) {
            int measureSpecParams = View.MeasureSpec.getSize(View.MeasureSpec.UNSPECIFIED);
            v.measure(measureSpecParams, measureSpecParams);
            height = v.getMeasuredHeight();
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (expand){
                    v.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expand){
                    v.setVisibility(View.INVISIBLE);
                    collapse(v, duration);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }






}
