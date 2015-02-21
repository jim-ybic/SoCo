package com.soco.SoCoClient.control.util;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.model.Project;
import com.soco.SoCoClient.view.LoginActivity;
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

    public static void createProjectOnServer(String pname, Context context,
                                             String loginEmail, String loginPassword) {
        HttpTask registerTask = new HttpTask(
                ProfileUtil.getCreateProjectUrl(context),
                HttpTask.HTTP_TYPE_CREATE_PROJECT,
                loginEmail, loginPassword, context, pname);
        registerTask.execute();
    }

    public static void addSharedFileToDb(Uri uri,
                                         String loginEmail, String loginPassword, int pid,
                                         ContentResolver cr, DBManagerSoco dbmgrSoco) {
        String displayName = FileUtils.getDisplayName(cr, uri);
        String remotePath = DropboxUtil.getRemotePath(uri,
                loginEmail, loginPassword, pid, cr);
        String localPath = FileUtils.copyFileToLocal(uri, cr);
        dbmgrSoco.addSharedFile(pid, displayName, uri, remotePath, localPath);
    }
}
