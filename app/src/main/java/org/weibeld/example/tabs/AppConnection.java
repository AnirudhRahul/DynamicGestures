package org.weibeld.example.tabs;

import android.support.annotation.NonNull;

/**
 * Created by user on 10/19/2017.
 */

public class AppConnection implements Comparable<AppConnection> {
    String initialApp="";
    String finalApp="";
    int totalConnectionsFromInitial=0;
    int timesUsed=0;

    public String getInitialApp() {
        return initialApp;
    }

    public void setInitialApp(String initialApp) {
        this.initialApp = initialApp;
    }

    public String getFinalApp() {
        return finalApp;
    }

    public void setFinalApp(String finalApp) {
        this.finalApp = finalApp;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public AppConnection(String initialApp, String finalApp, int timesUsed, int totalConnectionsFromInitial){
        this.initialApp=initialApp;
        this.finalApp=finalApp;
        this.timesUsed=timesUsed;
        this.totalConnectionsFromInitial=totalConnectionsFromInitial;
    }
    private double percent(){
        return (timesUsed*1.0)/totalConnectionsFromInitial;
    }
    public String toString(){
        return initialApp+"-->"+finalApp+":"+timesUsed;
    }

    @Override
    public int compareTo(@NonNull AppConnection o) {
        return (int)(-timesUsed+o.timesUsed);
    }
}
