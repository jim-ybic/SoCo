package com.soco.SoCoClient.control.util;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static void serverCreateProject(String pname, Context context,
                                           String loginEmail, String loginPassword, String pid,
                                           String projectSignature,
                                           String projectTag,
                                           String projectType) {

        HttpTask createProjectTask = new HttpTask(
                ProfileUtil.getCreateProjectUrl(context),
                HttpConfig.HTTP_TYPE_CREATE_PROJECT,
                loginEmail, loginPassword, context, pname, pid, null, null, null);

        createProjectTask.projectSignature = projectSignature;
        createProjectTask.projectTag = projectTag;
        createProjectTask.projectType = projectType;

        createProjectTask.execute();
    }

    public static void serverArchiveProject(String pid, Context context, String pid_onserver) {
        HttpTask archiveProjectTask = new HttpTask(
                ProfileUtil.getArchiveProjectUrl(context),
                HttpConfig.HTTP_TYPE_ARCHIVE_PROJECT,
                null, null, context, null, pid, pid_onserver, null, null);
        archiveProjectTask.execute();
    }

    public static void serverUpdateProjectName(String pid, Context context,
                                               String pname, String pid_onserver) {
        HttpTask archiveProjectTask = new HttpTask(
                ProfileUtil.getUpdateProjectNameUrl(context),
                HttpConfig.HTTP_TYPE_UPDATE_PROJECT_NAME,
                null, null, context, pname, pid, pid_onserver, null, null);
        archiveProjectTask.execute();
    }

    public static void serverSetProjectAttribute(String pid, Context context,
                                                 String pname, String pid_onserver,
                                                 HashMap<String, String> attrMap) {
        HttpTask setProjectAttributeTask = new HttpTask(
                ProfileUtil.getSetProjectAttributeUrl(context),
                HttpConfig.HTTP_TYPE_SET_PROJECT_ATTRIBUTE,
                null, null, //login email/password
                context, pname, pid, pid_onserver, attrMap, null);
        setProjectAttributeTask.execute();
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

    public static HashMap<String, ArrayList<Project>> groupingProjectsByTag(List<Project> projects){
        HashMap<String, ArrayList<Project>> map = new HashMap<String, ArrayList<Project>>();
        for(Project p : projects){
            String tag = p.ptag;
            if (map.containsKey(tag)) {
                Log.d(tag, "Add " + p.pname + " into existing group " + tag);
                map.get(tag).add(p);
            } else {
                Log.d(tag, "Create new group " + p.ptag + " for " + p.pname);
                ArrayList<Project> pp = new ArrayList<Project>();
                pp.add(p);
                map.put(tag, pp);
            }
        }

        return map;
    }



}
