package org.weibeld.example.tabs.Adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import org.weibeld.example.tabs.DataManager;
import org.weibeld.example.tabs.Fragments_and_UI.AppSelectorSettings;

import java.util.ArrayList;
import java.util.Collections;
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
    private HashMap<String,HashMap<String,String>> masterList=new HashMap<>();
    private HashMap<String,HashMap<String,Boolean>> BooleanMasterList=new HashMap<>();
    private String initialAppPackageName;
    PackageManager pm;
    List<ApplicationInfo> appList;
    public SettingAdapter(Context context, String initialAppPackageName, ArrayList<String> gestures){
        pm = context.getPackageManager();
        appList = getAllInstalledApplications(context);
        dataManager=new DataManager(context);
        this.context=context;
        this.gestures=gestures;
        this.initialAppPackageName=initialAppPackageName;
        try{masterList=dataManager.returnMap();BooleanMasterList=dataManager.returnBooleanMap();}catch (Exception e){Log.v("SettingAdapter","Error");e.printStackTrace();}
        Collections.sort(gestures);
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
        UsingGesture.setChecked(BooleanMasterList.get(gestures.get(i)).containsKey(initialAppPackageName)&&BooleanMasterList.get(gestures.get(i)).get(initialAppPackageName));
        if(!UsingGesture.isChecked())
            collapse(AppSelector,1);

//        Log.v("mapu",""+SwitchStates.get(i));
        final TextView selectedAppName=(TextView)rowView.findViewById(R.id.selectAppName);
        final ImageView selectedAppIcon=(ImageView)rowView.findViewById(selectAppIcon);

        final int a=i;
        UsingGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                collapse(AppSelector,1);
                Log.v("SettingStall","Collapsed");
                try{dataManager.AddToBooleanMap(gestures.get(a),initialAppPackageName,isChecked);}catch(Exception e){}
                Log.v("SettingStall","Boolean Map updated");

                String curPackageName="";
                try{
                    curPackageName=masterList.get(gestures.get(a)).get(initialAppPackageName);
                    if(isChecked)
                        setRow(masterList.get(gestures.get(a)).get(initialAppPackageName), selectedAppIcon, selectedAppName);
                }
                catch (Exception e){

                }
                Log.v("SettingStall","Row Is Set");



            }
        });
        selectAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("gestures",gestures.get(a));
                Intent i=new Intent(parent.getContext(), AppSelectorSettings.class);
                i.putExtra("GestureIndex",a);
                i.putExtra("StartingApp",initialAppPackageName);
                parent.getContext().startActivity(i);

            }
        });


        String curPackageName="";
        try{
        curPackageName=masterList.get(gestures.get(i)).get(initialAppPackageName);
            if(UsingGesture.isChecked()) {
                setRow(masterList.get(gestures.get(i)).get(initialAppPackageName), selectedAppIcon, selectedAppName);
                Doublecollapse(AppSelector,1);
            }
        }
        catch (Exception e){

        }





        return rowView;
    }
    public void setRow(String initialAppPackageName,ImageView AppIcon, TextView AppName){
        AppIcon.setImageDrawable(getAppDrawable(initialAppPackageName));
        AppName.setText(getAppName(initialAppPackageName));
    }
    public  List<ApplicationInfo> getAllInstalledApplications(Context context) {
        List<ApplicationInfo> installedApps = pm.getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>();
        for(int i =0; i<installedApps.size(); i++){
            if(context.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName) != null){
                //If you're here, then this is a launch-able app
                launchableInstalledApps.add(installedApps.get(i));
            }
        }
        return launchableInstalledApps;
    }
    public Drawable getAppDrawable(String packageName){

        for(ApplicationInfo e:appList){

            if(packageName.equals(e.packageName)){
                return e.loadIcon(pm);
            }
        }
        return null;
    }
    public String getAppName(String packageName){

        for(ApplicationInfo e:appList){

            if(packageName.equals(e.packageName)){
                return (String)e.loadLabel(pm);
            }
        }
        return null;
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
