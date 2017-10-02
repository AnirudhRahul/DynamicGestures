package org.weibeld.example.tabs;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by user on 9/30/2017.
 */

public class DataManager {
    private HashMap<String, HashMap<Integer,Integer>> masterList=new HashMap<>();
    private HashMap<String, HashMap<Integer,Boolean>> BooleanMasterList=new HashMap<>();
    private Context context;
    public DataManager(Context c){context=c;}
    public void AddConnection(String gesture, int startingApp, int endingApp) throws IOException{
        HashMap<Integer,Integer> temp=masterList.remove(gesture);

        temp.put(startingApp,endingApp);
        masterList.put(gesture,temp);
        WriteMap();
    }
    public void UpdateMap() throws IOException,ClassNotFoundException{
        if(!IsMapInitialized()) {
            ArrayList<String> list = returnGestureList();
            for (String e : list)
                masterList.put(e, new HashMap<Integer, Integer>());
            WriteMap();
        }
        else{
            FileInputStream fis = context.openFileInput("map");
            ObjectInputStream inputStreamStream = new ObjectInputStream(fis);
            masterList=(HashMap)inputStreamStream.readObject();
            ArrayList<String> keyset=new ArrayList<>();
            keyset.addAll(masterList.keySet());
            ArrayList<String> gesture=returnGestureList();

            ArrayList<String> intersection = new ArrayList<String>(keyset);
            intersection.retainAll(gesture);
            keyset.removeAll(intersection);
            gesture.removeAll(intersection);
            for(String e:gesture){
                masterList.put(e, new HashMap<Integer, Integer>());
            }
            for(String e:keyset){
                masterList.remove(e);
            }
            fis.close();
            inputStreamStream.close();


        }
    }
    public HashMap<String, HashMap<Integer,Integer>> returnMap() throws IOException,ClassNotFoundException{
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

    public void WriteMap() throws IOException{
        FileOutputStream fos = context.openFileOutput("map", context.MODE_PRIVATE);
        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
        outputStream.writeObject(masterList);
        outputStream.flush();
        outputStream.close();
    }












    public void UpdateBooleanMap() throws IOException,ClassNotFoundException{
        if(!IsBooleanMapInitialized()) {
            ArrayList<String> list = returnGestureList();
            for (String e : list)
                BooleanMasterList.put(e, new HashMap<Integer, Boolean>());
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
                BooleanMasterList.put(e, new HashMap<Integer, Boolean>());
            }
            for(String e:keyset){
                BooleanMasterList.remove(e);
            }
            fis.close();
            inputStreamStream.close();


        }
    }
    public HashMap<String, HashMap<Integer,Boolean>> returnBooleanMap() throws IOException,ClassNotFoundException{
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
    public void AddToBooleanMap(String gesture, int app, boolean used) throws IOException,ClassNotFoundException{
        UpdateBooleanMap();
        Log.v("BoolMap","Before:"+BooleanMasterList);
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
            final String[] gestures = {"Swipe Up", "Swipe Down", "Swipe Left", "Swipe Right", "Circle", "Shake"};
            list.addAll(Arrays.asList(gestures));
            WriteGesturesList(list);
        }
        FileInputStream fis = context.openFileInput("gestureList");
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<String> temp= (ArrayList<String>)is.readObject();
        fis.close();
        is.close();
        return temp;
    }
    public void appendGestureList(ArrayList<String> ListToAdd) throws IOException, ClassNotFoundException{
        ArrayList<String> temp=returnGestureList();
        temp.addAll(ListToAdd);
        WriteGesturesList(temp);
    }

}
