package com.soco.SoCoClient.control.util;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.http.task.ArchiveActivityTaskAsync;
import com.soco.SoCoClient.control.http.task.CreateActivityTaskAsync;
import com.soco.SoCoClient.control.http.task.SetActivityAttributeTaskAsync;
import com.soco.SoCoClient.control.http.task.UpdateActivityNameTaskAsync;
import com.soco.SoCoClient.model.Profile;
import com.soco.SoCoClient.model.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityUtil {

    static String tag = "ProjectUtil";

    public static int findPidByPname(List<Activity> activities, String pname) {
        int pid = -1;
        for (int i = 0; i < activities.size(); i++)
            if (activities.get(i).pname.equals(pname))
                pid = activities.get(i).pid;
        if (pid == -1)
            Log.e(tag, "Cannot find pid for project name: " + pname);
        else
            Log.i(tag, "Found pid for project name " + pname + ": " + pid);
        return pid;
    }

    public static void serverCreateActivity(String pname, Context context,
                                            String loginEmail, String loginPassword, String pid,
                                            String projectSignature,
                                            String projectTag,
                                            String projectType) {

//        HttpTask createProjectTask = new HttpTask(
//                ProfileUtil.getCreateProjectUrl(context),
//                HttpConfig.HTTP_TYPE_CREATE_PROJECT,
//                loginEmail, loginPassword, context, pname, pid, null, null, null);
//
//        createProjectTask.projectSignature = projectSignature;
//        createProjectTask.projectTag = projectTag;
//        createProjectTask.projectType = projectType;
//
//        createProjectTask.execute();
        Profile profile = ((SocoApp)context).profile;
        String url = profile.getCreateProjectUrl(context);
        CreateActivityTaskAsync task = new CreateActivityTaskAsync(
                url, pname, pid, context,
                projectSignature, projectTag, projectType);
        task.execute();
    }

    public static void serverArchiveActivity(String pid, Context context, String pid_onserver) {
//        HttpTask archiveProjectTask = new HttpTask(
//                ProfileUtil.getArchiveProjectUrl(context),
//                HttpConfig.HTTP_TYPE_ARCHIVE_PROJECT,
//                null, null, context, null, pid, pid_onserver, null, null);
//        archiveProjectTask.execute();
        Profile profile = ((SocoApp)context).profile;
        String url = profile.getArchiveProjectUrl(context);
        ArchiveActivityTaskAsync task = new ArchiveActivityTaskAsync(
                url, pid_onserver);
        task.execute();
    }

    public static void serverUpdateActivityName(String pid, Context context,
                                                String pname, String pid_onserver) {
//        HttpTask archiveProjectTask = new HttpTask(
//                ProfileUtil.getUpdateProjectNameUrl(context),
//                HttpConfig.HTTP_TYPE_UPDATE_PROJECT_NAME,
//                null, null, context, pname, pid, pid_onserver, null, null);
//        archiveProjectTask.execute();
        Profile profile = ((SocoApp)context).profile;
        String url = profile.getUpdateProjectNameUrl(context);
        UpdateActivityNameTaskAsync task = new UpdateActivityNameTaskAsync(url, pname, pid_onserver);
        task.execute();
    }

    public static void serverSetActivityAttribute(String pid, Context context,
                                                  String pname, String pid_onserver,
                                                  HashMap<String, String> attrMap) {
//        HttpTask setProjectAttributeTask = new HttpTask(
//                ProfileUtil.getSetProjectAttributeUrl(context),
//                HttpConfig.HTTP_TYPE_SET_PROJECT_ATTRIBUTE,
//                null, null, //login email/password
//                context, pname, pid, pid_onserver, attrMap, null);
//        setProjectAttributeTask.execute();

        Profile profile = ((SocoApp)context).profile;
        String url = profile.getSetProjectAttributeUrl(context);
        SetActivityAttributeTaskAsync task = new SetActivityAttributeTaskAsync(
                url, pid_onserver, attrMap);
        task.execute();
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


    public static HashMap<String, ArrayList<Activity>> groupingActivitiesByTag(List<Activity> activities){
        Log.d(tag, "grouping project by tag");
        HashMap<String, ArrayList<Activity>> map = new HashMap<String, ArrayList<Activity>>();
        for(Activity p : activities){
            Log.d(tag, "processing project " + p.pname + " with tag " + p.ptag);
            String tag = p.ptag;
            if (map.containsKey(tag)) {
                Log.d(tag, "Add " + p.pname + " into existing group " + tag);
                map.get(tag).add(p);
            } else {
                Log.d(tag, "Create new group " + p.ptag + " for " + p.pname);
                ArrayList<Activity> pp = new ArrayList<Activity>();
                pp.add(p);
                map.put(tag, pp);
            }
        }

        return map;
    }



}
