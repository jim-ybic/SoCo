package com.soco.SoCoClient.view.ui.tab;

//import info.androidhive.tabsswipe.R;
import com.soco.SoCoClient.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProjectUpdatesFragment extends Fragment {

    String tag = "ProjectUpdatesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(tag, "create top rated fragment view");
        View rootView = inflater.inflate(R.layout.fragment_project_updates, container, false);

        return rootView;
    }
}