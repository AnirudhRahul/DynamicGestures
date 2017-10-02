package org.weibeld.example.tabs;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.weibeld.example.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.weibeld.example.R.id.appName;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DataManager dataManager=new DataManager(getApplicationContext());
        try{
        System.out.println(dataManager.returnMap().toString());}catch (Exception e){}
        //Set up the top activity bar
        ImageButton backButton=(ImageButton) findViewById(R.id.backbtn);
        ImageView appIconView=(ImageView) findViewById(R.id.appIcon);
        TextView appNameView=(TextView) findViewById(appName);
        Intent intent=getIntent();
        int index =intent.getIntExtra("AppId",-1);

        String appName=getAppName(index);
        Drawable drawable=getAppDrawable(index);

        appNameView.setText(appName);
        appIconView.setImageDrawable(drawable);
        //Back Button setup
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        //List View setup
        ListView settingsList=(ListView)findViewById(R.id.settingsList);
        ArrayList<String> gesture=new ArrayList<>();
        HashMap<String, HashMap<Integer,Integer>> masterList= new HashMap<>();
        try{gesture=dataManager.returnGestureList();masterList=dataManager.returnMap();}catch (Exception e){e.printStackTrace();}
        Log.v("null",""+getApplicationContext());


        SettingAdapter adapter=new SettingAdapter(getApplicationContext(),index,gesture, masterList);
        Log.v("Adapater",""+index);
        Log.v("Adapater",""+gesture);
        Log.v("Adapater",""+masterList);

        settingsList.setAdapter(adapter);



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    public Drawable getAppDrawable(int index){
        Drawable drawable=null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        ResolveInfo AppInfo=packages.get(index);
        drawable=AppInfo.loadIcon(getApplicationContext().getPackageManager());
        return drawable;
    }
    public String getAppName(int index){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        ResolveInfo AppInfo=packages.get(index);
        return AppInfo.loadLabel(getApplicationContext().getPackageManager()).toString();
    }





}
