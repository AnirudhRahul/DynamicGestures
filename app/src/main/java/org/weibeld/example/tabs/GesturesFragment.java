package org.weibeld.example.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.weibeld.example.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/* Fragment used as page 2 */
public class GesturesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gesturesfragment, container, false);
        Button btn=(Button)rootView.findViewById(R.id.setupButton);
        final String[] gestures={"Swipe Up","Swipe Down","Swipe Left","Swipe Right","Circle","Shake"};
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = getActivity().getApplicationContext().getFilesDir();
                File file = new File(path, "Gestures.txt");
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(Arrays.toString(gestures).getBytes());
                    stream.close();
                }catch(Exception e){
                  e.printStackTrace();
                }


            }
        });
        return rootView;
    }

}
