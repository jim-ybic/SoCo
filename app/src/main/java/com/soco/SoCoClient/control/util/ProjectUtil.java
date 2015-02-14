package com.soco.SoCoClient.control.util;


import android.util.Log;

import com.soco.SoCoClient.model.Project;
import com.soco.SoCoClient.view.ShowActiveProjectsActivity;

import java.util.List;

public class ProjectUtil {

    static String tag = "ProjectUtil";

    public static int findPidByPname(List<Project> projects, String pname) {
        int pid = -1;
        for (int i = 0; i < projects.size(); i++)
            if (projects.get(i).pname.equals(pname))
                pid = projects.get(i).pid;
        if (pid == -1)
            Log.e(tag, "Cannot find pid for project name: " + pname);
        else
            Log.i(tag, "Found pid for project name " + pname + ": " + pid);
        return pid;
    }
}
