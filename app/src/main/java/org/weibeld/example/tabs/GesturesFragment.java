package org.weibeld.example.tabs;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.weibeld.example.R;

/* Fragment used as page 2 */
public class GesturesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gesturesfragment, container, false);
        Button btn=(Button)(rootView.findViewById(R.id.testbutton));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(i);
            }
        });
        return rootView;
    }

}
