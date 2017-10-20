package org.weibeld.example.tabs.Fragments_and_UI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.weibeld.example.R;
import org.weibeld.example.tabs.Adapters.GestureAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/* Fragment used as page 2 */
public class GesturesFragment extends Fragment {
    final private String[] finalGestureList={"Left Swipe", "Right Swipe"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gesturesfragment, container, false);
        ListView listView=(ListView)rootView.findViewById(R.id.gestureList);
        ArrayList<String> gesturesTemp=new ArrayList<String>();
        gesturesTemp.addAll(Arrays.asList(finalGestureList));
        GestureAdapter adapter=new GestureAdapter(getActivity().getApplicationContext(),gesturesTemp);
        listView.setAdapter(adapter);
        return rootView;
    }

}
