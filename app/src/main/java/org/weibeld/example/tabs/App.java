package org.weibeld.example.tabs;

import android.graphics.drawable.Drawable;

/**
 * Created by user on 9/22/2017.
 */

public class App {
    private String name;
    private Drawable icon;

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

    public App(String name, Drawable icon){
        this.name=name;
        this.icon=icon;
    }
}
