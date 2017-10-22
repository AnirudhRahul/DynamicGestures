package org.weibeld.example.tabs;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 9/30/2017.
 */

public class DataManager {
    //Use package Name everywher but menus
    private HashMap<String, HashMap<String,String>> masterList=new HashMap<String, HashMap<String,String>>();
    private HashMap<String, HashMap<String,Boolean>> BooleanMasterList=new HashMap<>();
    private Context context;
    public DataManager(Context c){context=c;}
    public void AddConnection(String gesture, String startingApp, String endingApp) throws IOException{
        try {
            Log.v("LIST_READ",""+!masterList.containsValue(gesture));
            if(!masterList.containsValue(gesture))
            UpdateMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String,String> temp=masterList.remove(gesture);
        temp.put(startingApp,endingApp);
        masterList.put(gesture,temp);
        WriteMap(masterList);
    }
    public void UpdateMap() throws IOException,ClassNotFoundException{
        if(!IsMapInitialized()) {
            ArrayList<String> list = returnGestureList();
            for (String e : list)
                masterList.put(e, new HashMap<String, String>());
            WriteMap(masterList);
        }
        else{
            FileInputStream fis = context.openFileInput("map");
            ObjectInputStream inputStreamStream = new ObjectInputStream(fis);
            masterList=(HashMap)inputStreamStream.readObject();

            fis.close();
            inputStreamStream.close();


        }
    }
    public HashMap<String, HashMap<String,String>> returnMap() throws IOException,ClassNotFoundException{
        UpdateMap();
        return masterList;
    }
    public boolean IsMapInitialized() throws IOException,ClassNotFoundException{
        File file = new File(context.getFilesDir(), "map");
        try {
            ObjectInputStream inputStreamStream = new ObjectInputStream(new FileInputStream(file));
            boolean temp=inputStreamStream.readObject()!=null;
            inputStreamStream.close();
            return temp;
        }catch (Exception e){return false;}

    }

    public void WriteMap(HashMap<String, HashMap<String,String>> a) throws IOException{
        FileOutputStream fos = context.openFileOutput("map", context.MODE_PRIVATE);
        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
        outputStream.writeObject(a);
        outputStream.flush();
        outputStream.close();
    }












    public void UpdateBooleanMap() throws IOException,ClassNotFoundException{
        if(!IsBooleanMapInitialized()) {
            ArrayList<String> list = returnGestureList();
            for (String e : list)
                BooleanMasterList.put(e, new HashMap<String, Boolean>());
            WriteBooleanMap();
        }
        else{
            FileInputStream fis = context.openFileInput("Bmap");
            ObjectInputStream inputStreamStream = new ObjectInputStream(fis);
            BooleanMasterList=(HashMap)inputStreamStream.readObject();
            ArrayList<String> keyset=new ArrayList<>();
            keyset.addAll(BooleanMasterList.keySet());
            ArrayList<String> gesture=returnGestureList();

            ArrayList<String> intersection = new ArrayList<String>(keyset);
            intersection.retainAll(gesture);
            keyset.removeAll(intersection);
            gesture.removeAll(intersection);
            for(String e:gesture){
                BooleanMasterList.put(e, new HashMap<String, Boolean>());
            }
            for(String e:keyset){
                BooleanMasterList.remove(e);
            }
            fis.close();
            inputStreamStream.close();


        }
    }
    public HashMap<String, HashMap<String,Boolean>> returnBooleanMap() throws IOException,ClassNotFoundException{
        UpdateBooleanMap();
        return BooleanMasterList;
    }
    public boolean IsBooleanMapInitialized() throws IOException,ClassNotFoundException{
        try {
            File file = new File(context.getFilesDir(), "Bmap");
            ObjectInputStream inputStreamStream = new ObjectInputStream(new FileInputStream(file));
            Boolean temp= inputStreamStream.readObject()!=null;
            inputStreamStream.close();
            return temp;
        }catch (Exception e){return false;}

    }

    public void WriteBooleanMap() throws IOException{
        FileOutputStream fos = context.openFileOutput("Bmap", context.MODE_PRIVATE);
        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
        outputStream.writeObject(BooleanMasterList);
        outputStream.flush();
        outputStream.close();
    }
    public void AddToBooleanMap(String gesture, String app, boolean used) throws IOException,ClassNotFoundException{
        UpdateBooleanMap();
        Log.v("BoolMap","Before:"+BooleanMasterList);
        BooleanMasterList.get(gesture).remove(app);
        BooleanMasterList.get(gesture).put(app,used);
        WriteBooleanMap();
        Log.v("BoolMap","After:"+BooleanMasterList);
    }



























    public void WriteGesturesList(ArrayList<String> list) throws IOException {
        Log.v("null",context==null?"null":"FINE");
        FileOutputStream fos = context.openFileOutput("gestureList", context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(list);
        os.close();
        fos.close();
    }
    public boolean isGestureListInitiliazed() throws IOException, ClassNotFoundException{

        try{
            File file = new File(context.getFilesDir(), "gestureList");
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            Boolean temp= is.readObject()!=null;
            is.close();
            return temp;
        }
        catch (Exception e){return false;}
    }
    public ArrayList<String> returnGestureList() throws IOException,ClassNotFoundException {
        Log.v("TAG", isGestureListInitiliazed() + "");
        if (!isGestureListInitiliazed()) {
            ArrayList<String> list = new ArrayList<>();
            final String[] gestures={};
            list.addAll(Arrays.asList(gestures));
            WriteGesturesList(list);
        }
        FileInputStream fis = context.openFileInput("gestureList");
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<String> temp= (ArrayList<String>)is.readObject();
        fis.close();
        is.close();
        Collections.sort(temp);
        return temp;
    }
    public void initializeAppConnectionMap() throws IOException{
        if(!isAppConnectionInitialized()){
        Log.v("null",context==null?"null":"FINE");
        HashMap<Short, HashMap<Short, Integer>> AppConnectionList=new HashMap<>();
        FileOutputStream fos = context.openFileOutput("appConnectionList", context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(AppConnectionList);
        os.close();
        fos.close();
        }
    }
    public boolean isAppConnectionInitialized() throws IOException{
        try{
            File file = new File(context.getFilesDir(), "appConnectionList");
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            Boolean temp= is.readObject()!=null;
            is.close();
            return temp;
        }
        catch (Exception e){return false;}
    }
    public void logAppConnections() throws IOException, ClassNotFoundException {
//        File file = new File(context.getFilesDir(), "appConnectionList");
//        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
//        HashMap<Short, HashMap<Short,Integer>> appConnectionsList=(HashMap<Short, HashMap<Short, Integer>>) is.readObject();
//        is.close();
//        String output="";
//        for(short e:appConnectionsList.keySet()){
//
//            output+=indexToString(e)+"\n";
//            HashMap<Short,Integer> currentMap=appConnectionsList.get(e);
//            for(short c:currentMap.keySet()){
//                output+="{"+indexToString(c)+":Times Visited "+currentMap.get(c)+"},";
//            }
//            output+="\n";
//        }
//        output+="\n";
//       // Log.v("MAPURU",output);
        Log.v("MAPURU",returnAppConnections().toString());
    }
    public HashMap<String,HashMap<String,Integer>> returnAppConnections() throws IOException, ClassNotFoundException {
        File file = new File(context.getFilesDir(), "appConnectionList");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        HashMap<Short, HashMap<Short,Integer>> appConnectionsList=(HashMap<Short, HashMap<Short, Integer>>) is.readObject();
        HashMap<String,HashMap<String,Integer>> outputList=new HashMap<>();
        is.close();
        Log.v("stalling","ShortMap read");

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appList = getAllInstalledApplications(context);

        for(short s1:appConnectionsList.keySet()){
            HashMap<Short,Integer> currentMap=appConnectionsList.get(s1);
            HashMap<String,Integer> outputCurrentMap=new HashMap<>();
            for(short s2:currentMap.keySet()){
                try {
                    outputCurrentMap.put(appList.get(s2).loadLabel(pm).toString(), currentMap.get(s2));
                }catch (Exception e){}
            }
               // output+="{"+indexToString(c)+":Times Visited "+currentMap.get(c)+"},";
            try{
            outputList.put(appList.get(s1).loadLabel(pm).toString(),outputCurrentMap);}catch (Exception e){}

        }
        Log.v("stalling","ShortMap Converted");

        Log.v("he123",outputList.toString());
        return outputList;
        }


    public static List<ApplicationInfo> getAllInstalledApplications(Context context) {
        List<ApplicationInfo> installedApps = context.getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>();
        for(int i =0; i<installedApps.size(); i++){
            if(context.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName) != null){
                //If you're here, then this is a launch-able app
                launchableInstalledApps.add(installedApps.get(i));
            }
        }
        return launchableInstalledApps;
    }

    public void addAppConnection(short startingAppIndex,short endingAppIndex) throws IOException, ClassNotFoundException {
        if(!isAppConnectionInitialized())
            initializeAppConnectionMap();
            initializeAppConnectionMap();
            FileInputStream fis = context.openFileInput("appConnectionList");
            ObjectInputStream is = new ObjectInputStream(fis);
            HashMap<Short, HashMap<Short, Integer>> AppConnectionList = (HashMap<Short, HashMap<Short, Integer>>) is.readObject();
            is.close();
            fis.close();
            if(!AppConnectionList.containsKey(startingAppIndex)) {
                HashMap<Short,Integer> temp=new HashMap<>();
                temp.put(endingAppIndex,1);
                AppConnectionList.put(startingAppIndex,temp);
            }else {
                HashMap<Short, Integer> startingAppMap = AppConnectionList.get(startingAppIndex);
                if (!startingAppMap.containsKey(endingAppIndex)) {
                    startingAppMap.put(endingAppIndex,1);
                }
                else{
                    startingAppMap.put(endingAppIndex,startingAppMap.remove(endingAppIndex)+1);
                }

            }

            AppConnectionList.get(startingAppIndex).get(startingAppIndex);
        FileOutputStream fos = context.openFileOutput("appConnectionList", context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(AppConnectionList);
        os.close();
        fos.close();
        //Make sure to properly Define the eqaulity for APPusage object
       // if(AppConnectionList.get())
    }


//    public void initializeAppList() throws IOException {
//        PackageManager pm = context.getPackageManager();
//        int size=pm.getInstalledApplications(0).size();
//        boolean appList[] = new boolean[size];
//        FileOutputStream fos = context.openFileOutput("appList", context.MODE_PRIVATE);
//        ObjectOutputStream os = new ObjectOutputStream(fos);
//        os.writeObject(appList);
//        os.close();
//        fos.close();
//    }
//    public boolean[] returnAppList()  {
//        try{
//        FileInputStream fis = context.openFileInput("appList");
//        ObjectInputStream inputStreamStream = new ObjectInputStream(fis);
//        return (boolean[])inputStreamStream.readObject();}catch (Exception e){e.printStackTrace();}
//        return null;
//    }
//    public boolean[] filterAppList(String input){
//        PackageManager pm = context.getPackageManager();
//        try {
//            initializeAppList();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<ApplicationInfo> appList=pm.getInstalledApplications(0);
//        boolean[] filteredList=returnAppList();
//        int i=0;
//        for(ApplicationInfo e:appList){
//            filteredList[i]=!e.loadLabel(pm).toString().contains(input);
//            i++;
//        }
//        try{
//        FileOutputStream fos = context.openFileOutput("appList", context.MODE_PRIVATE);
//        ObjectOutputStream os = new ObjectOutputStream(fos);
//        os.writeObject(filteredList);
//        os.close();
//        fos.close();}catch (Exception e){e.printStackTrace();}
//        Log.v("impertant",Arrays.toString(returnAppList()));
//        return filteredList;
//    }


}
