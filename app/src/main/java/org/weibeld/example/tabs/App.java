package org.weibeld.example.tabs;

import android.graphics.drawable.Drawable;

/**
 * Created by user on 9/22/2017.
 */

public class App implements Comparable<App> {
    private String name;
    private Drawable icon;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public App(String name, Drawable icon, int index){
        this.name=name;
        this.icon=icon;
        this.index=index;
    }


    @Override
    public int compareTo(App app) {
        return name.compareTo(app.getName());
    }
}
