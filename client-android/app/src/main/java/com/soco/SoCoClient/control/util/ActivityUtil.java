package com.soco.SoCoClient.control.util;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.soco.SoCoClient.control.config.SocoApp;
import com.soco.SoCoClient.control.database._ref.DBManagerSoco;
import com.soco.SoCoClient.control.dropbox._ref.DropboxUtilV1;
import com.soco.SoCoClient.control.http.task._ref.ArchiveActivityTaskAsync;
import com.soco.SoCoClient.control.http.task._ref.CreateActivityTaskAsync;
import com.soco.SoCoClient.control.http.task._ref.SetActivityAttributeTaskAsync;
import com.soco.SoCoClient.control.http.task._ref.UpdateActivityNameTaskAsync;
import com.soco.SoCoClient.model._ref.Folder;
import com.soco.SoCoClient.model._ref.Activity;
import com.soco.SoCoClient.control.http.UrlUtil;
import com.soco.SoCoClient.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityUtil {

    static String tag = "ActivityUtil";

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

//    public static void serverCreateActivity(String pname, Context context,
//                                            String loginEmail, String loginPassword, String pid,
//                                            String projectSignature,
//                                            String projectTag,
//                                            String projectType) {
//
////        HttpTask createProjectTask = new HttpTask(
////                ProfileUtil.getCreateProjectUrl(context),
////                HttpConfigV1.HTTP_TYPE_CREATE_PROJECT,
////                loginEmail, loginPassword, context, pname, pid, null, null, null);
////
////        createProjectTask.projectSignature = projectSignature;
////        createProjectTask.projectTag = projectTag;
////        createProjectTask.projectType = projectType;
////
////        createProjectTask.execute();
//        Profile profile = ((SocoApp)context).profile;
//        String url = UrlUtil.getCreateProjectUrl(context);
//        CreateActivityTaskAsync task = new CreateActivityTaskAsync(
//                url, pname, pid, context,
//                projectSignature, projectTag, projectType);
//        task.execute();
//    }

    public static void serverArchiveActivity(String pid, Context context, String pid_onserver) {
//        HttpTask archiveProjectTask = new HttpTask(
//                ProfileUtil.getArchiveProjectUrl(context),
//                HttpConfigV1.HTTP_TYPE_ARCHIVE_PROJECT,
//                null, null, context, null, pid, pid_onserver, null, null);
//        archiveProjectTask.execute();
        Profile profile = ((SocoApp)context).profile;
        String url = UrlUtil.getArchiveProjectUrl(context);
        ArchiveActivityTaskAsync task = new ArchiveActivityTaskAsync(
                url, pid_onserver);
        task.execute();
    }

    public static void serverUpdateActivityName(String pid, Context context,
                                                String pname, String pid_onserver) {
//        HttpTask archiveProjectTask = new HttpTask(
//                ProfileUtil.getUpdateProjectNameUrl(context),
//                HttpConfigV1.HTTP_TYPE_UPDATE_PROJECT_NAME,
//                null, null, context, pname, pid, pid_onserver, null, null);
//        archiveProjectTask.execute();
        Profile profile = ((SocoApp)context).profile;
        String url = UrlUtil.getUpdateProjectNameUrl(context);
        UpdateActivityNameTaskAsync task = new UpdateActivityNameTaskAsync(url, pname, pid_onserver);
        task.execute();
    }

    public static void serverSetActivityAttribute(String pid, Context context,
                                                  String pname, String pid_onserver,
                                                  HashMap<String, String> attrMap) {
//        HttpTask setProjectAttributeTask = new HttpTask(
//                ProfileUtil.getSetProjectAttributeUrl(context),
//                HttpConfigV1.HTTP_TYPE_SET_PROJECT_ATTRIBUTE,
//                null, null, //login email/password
//                context, pname, pid, pid_onserver, attrMap, null);
//        setProjectAttributeTask.execute();

        Profile profile = ((SocoApp)context).profile;
        String url = UrlUtil.getSetProjectAttributeUrl(context);
        SetActivityAttributeTaskAsync task = new SetActivityAttributeTaskAsync(
                url, pid_onserver, attrMap);
        task.execute();
    }

    public static void addSharedFileToDb(Uri uri,
                                         String loginEmail, String loginPassword, int pid,
                                         ContentResolver cr, DBManagerSoco dbmgrSoco) {
        String displayName = FileUtils.getDisplayName(cr, uri);
        String remotePath = DropboxUtilV1.getRemotePath(uri,
                loginEmail, loginPassword, pid, cr);
        String localPath = FileUtils.copyFileToLocal(uri, cr);
        dbmgrSoco.addSharedFile(pid, displayName, uri, remotePath, localPath);
    }


    public static HashMap<String, ArrayList<Activity>> groupingActivitiesByTag(List<Activity> activities){
        Log.d(tag, "grouping project by tag");
        HashMap<String, ArrayList<Activity>> map = new HashMap<String, ArrayList<Activity>>();
        for(Activity p : activities){
            Log.v(tag, "processing project " + p.pname + " with tag " + p.ptag);
            String tag = p.ptag;
            if (map.containsKey(tag)) {
                Log.v(tag, "Add " + p.pname + " into existing group " + tag);
                map.get(tag).add(p);
            } else {
                Log.v(tag, "Create new group " + p.ptag + " for " + p.pname);
                ArrayList<Activity> pp = new ArrayList<Activity>();
                pp.add(p);
                map.put(tag, pp);
            }
        }

        return map;
    }


    public static HashMap<String, ArrayList<Folder>> groupingFoldersByTag(List<Folder> folders) {
        Log.d(tag, "grouping folders by tag");
        HashMap<String, ArrayList<Folder>> map = new HashMap<>();
        for (Folder f : folders) {
            Log.d(tag, "processing folder: " + f.fname + ", " + f.ftag + ", " + f.ftag);
            String tag = f.ftag;
            if (map.containsKey(tag))
                map.get(tag).add(f);
            else {
                ArrayList<Folder> ff = new ArrayList<>();
                ff.add(f);
                map.put(tag, ff);
            }
        }
        return map;
    }


}
