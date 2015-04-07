package com.soco.SoCoClient.control.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.task.ArchiveProjectTask;
import com.soco.SoCoClient.control.http.task.CreateProjectTask;
import com.soco.SoCoClient.control.http.task.LoginTask;
import com.soco.SoCoClient.control.http.task.RegisterTask;
import com.soco.SoCoClient.control.http.task.SetProjectAttributeTask;
import com.soco.SoCoClient.control.http.task.UpdateProjectNameTask;
import com.soco.SoCoClient.control.util.ProfileUtil;

import java.util.HashMap;

public class HttpTask extends AsyncTask<Void, Void, Boolean> {
    static String tag = "HttpTask";

    public static String HTTP_TOKEN_TYPE = "access_token";

    public static String KEYWORD_REGISTRATION_SUBMITTED = "Your account registration email";

    String url;
    String type;
    String loginEmail, loginPassword;
    Context context;
    String pname, pid, pid_onserver;
    HashMap<String, String> attrMap;
    DBManagerSoco dbManagerSoco;

    public String projectSignature, projectTag, projectType;

    public HttpTask(String url,
                    String type,
                    String loginEmail,
                    String loginPassword,
                    Context context,
                    String pname,
                    String pid,
                    String pid_onserver,
                    HashMap<String, String> attrMap){
        Log.i(tag, "Create new HttpTask: " + url + ", " + type + ", "
                + loginEmail + ", " + loginPassword + ", " + pname);
        this.url = url;
        this.type = type;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.context = context;
        this.pname = pname;
        this.pid = pid;
        this.pid_onserver = pid_onserver;
        this.attrMap = attrMap;

        this.dbManagerSoco = ((SocoApp)context).dbManagerSoco;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //todo: check below conditions
//        if(url == null || url.isEmpty() || type == null || type.isEmpty()){
//            Log.e(tag, "Cannot get url/type");
//            return false;
//        }
        Log.i(tag, "Start http task, url is " + url + ", type is " + type);

        if(type.equals(HttpConfig.HTTP_TYPE_LOGIN)) {
            LoginTask.execute(loginEmail, loginPassword, url, context);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_REGISTER)) {
            RegisterTask.execute(loginEmail, loginPassword, url, context);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_CREATE_PROJECT)) {
            CreateProjectTask.execute(url, pname, pid, context,
                    projectSignature, projectTag, projectType);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_ARCHIVE_PROJECT)) {
            ArchiveProjectTask.execute(url, pid, pid_onserver);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_UPDATE_PROJECT_NAME)) {
            UpdateProjectNameTask.execute(url, pname, pid, pid_onserver);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_SET_PROJECT_ATTRIBUTE)) {
            SetProjectAttributeTask.execute(url, pname, pid, pid_onserver, attrMap);
        }
        else{
            Log.e(tag, "Unknown http task type: " + type);
        }

        return true;
    }

}
