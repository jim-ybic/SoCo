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

public class HttpTask extends AsyncTask<Void, Void, Boolean> {
    static String tag = "HttpTask";

    public static String HTTP_TOKEN_TYPE = "access_token";

    public static String KEYWORD_REGISTRATION_SUBMITTED = "Your account registration email";


    String url;
    String type;
    String loginEmail, loginPassword;
    Context context;
    String pname, pid;
    DBManagerSoco dbManagerSoco;

    public HttpTask(String url, String type,
                    String loginEmail, String loginPassword,
                    Context context,
                    String pname, String pid){
        Log.i(tag, "Create new HttpTask: " + url + ", " + type + ", "
                + loginEmail + ", " + loginPassword + ", " + pname);
        this.url = url;
        this.type = type;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.context = context;
        this.pname = pname;
        this.pid = pid;
        this.dbManagerSoco = ((SocoApp)context).dbManagerSoco;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || type == null || type.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }
        Log.i(tag, "Start http task, url is " + url + ", type is " + type);

        if(type.equals(HttpConfig.HTTP_TYPE_LOGIN)) {
            LoginTask.execute(loginEmail, loginPassword, url, context);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_REGISTER)) {
            RegisterTask.execute(loginEmail, loginPassword, url, context);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_CREATE_PROJECT)) {
            CreateProjectTask.execute(url, pname, pid, context);
        }
        else if(type.equals(HttpConfig.HTTP_TYPE_ARCHIVE_PROJECT)) {
            ArchiveProjectTask.execute(url, pid);
        }

        return true;
    }

}
