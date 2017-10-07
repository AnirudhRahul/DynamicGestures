package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.weibeld.example.R;

/* Fragment used as page 3 */
public class Analytics extends Fragment {
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.analyticsfragment, container, false);

        return rootView;
    }
}
