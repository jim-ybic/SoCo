package com.soco.SoCoClient.view.ui.tab;

//import info.androidhive.tabsswipe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;

public class ProjectResourcesFragment extends Fragment {

    String tag = "ProjectResourcesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create project resources fragment view");
        View rootView = inflater.inflate(R.layout.fragment_project_resources, container, false);

        return rootView;
    }
}