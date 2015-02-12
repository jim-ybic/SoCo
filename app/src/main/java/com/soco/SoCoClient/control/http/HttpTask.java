package com.soco.SoCoClient.control.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.util.ProfileUtil;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class HttpTask extends AsyncTask<Void, Void, Boolean> {
    static String tag = "HttpTask";

    public static String HTTP_TYPE_LOGIN = "login";
    public static String HTTP_TYPE_REGISTER = "register";
    public static String HTTP_TYPE_CREATE_PROJECT = "create_project";

    public static String JSON_KEY_USERNAME = "username";
    public static String JSON_KEY_EMAIL = "email";
    public static String JSON_KEY_PASSWORD = "password";
    public static String JSON_KEY_PASSWORD2 = "password2";
    public static String JSON_KEY_ACCESS_TOKEN = "access_token";
    public static String JSON_KEY_PROJECT_NAME = "name";

    public static String JSON_KEY_CREATE_PROJECT_STATUS = "status";
    public static String JSON_VALUE_CREATE_PROJECT_SUCCESS = "success";

    public static String HTTP_TOKEN_TYPE = "access_token";

    public static String KEYWORD_REGISTRATION_SUBMITTED = "Your account registration email";


    String url;
    String type;
    String loginEmail, loginPassword;
    Context context;
    String pname;

    public HttpTask(String url, String type, String loginEmail, String loginPassword,
                    Context context, String pname){
        Log.i(tag, "Create new HttpTask: " + url + ", " + type + ", "
                + loginEmail + ", " + loginPassword + ", " + pname);
        this.url = url;
        this.type = type;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.context = context;
        this.pname = pname;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || type == null || type.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }
        Log.i(tag, "Start http task, url is " + url + ", type is " + type);

        if(type.equals(HTTP_TYPE_LOGIN)) {
            Object response = loginStart();
            if (response != null)
                loginResponse(response);
        }
        else if(type.equals(HTTP_TYPE_REGISTER)) {
            Object response = registerStart();
            if (response != null)
                registerResponse(response);
        }
        else if(type.equals(HTTP_TYPE_CREATE_PROJECT)) {
            Object response = createProjectStart(url, pname);
            if (response != null)
                createProjectResponse(response);
        }

        return true;
    }

    private Object registerStart() {
        JSONObject data = new JSONObject();
        try {
            data.put(JSON_KEY_USERNAME, loginEmail);
            data.put(JSON_KEY_EMAIL, loginEmail);
            data.put(JSON_KEY_PASSWORD, loginPassword);
            data.put(JSON_KEY_PASSWORD2, loginPassword);
            Log.i(tag, "Register Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create RegisterJson post data");
            e.printStackTrace();
        }

        return executeHttpPost(url, data);
    }

    private boolean registerResponse(Object response) {
        Log.d(tag, "Process register response: " + response.toString());
        try {
            String str = response.toString();
            if (str.contains(KEYWORD_REGISTRATION_SUBMITTED)){
                SocoApp app = (SocoApp) context;
                app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_SUCCESS);
                Log.i(tag, "Set registration status: success");
                return true;
            }
            else {
                SocoApp app = (SocoApp) context;
                app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_FAIL);
                Log.i(tag, "Set registration status: fail");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert response to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private Object createProjectStart(String url, String pname) {
        JSONObject data = new JSONObject();
        try {
            data.put(JSON_KEY_PROJECT_NAME, pname);
            //todo: put other project details
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return executeHttpPost(url, data);
    }

    private boolean createProjectResponse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Create project response string: " + str);
            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(JSON_KEY_CREATE_PROJECT_STATUS);
            if(isSuccess.equals(JSON_VALUE_CREATE_PROJECT_SUCCESS)) {
                Log.i(tag, "Create project server response: " + JSON_VALUE_CREATE_PROJECT_SUCCESS);
                return true;
            }
            else {
                Log.e(tag, "Cannot receive create project status response from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert response to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private Object loginStart() {
        JSONObject data = new JSONObject();
        try {
            data.put(JSON_KEY_USERNAME, loginEmail);
            data.put(JSON_KEY_PASSWORD, loginPassword);
            Log.i(tag, "Login Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create Login Json post data");
            e.printStackTrace();
        }

        return executeHttpPost(url, data);
    }

    private boolean loginResponse(Object response) {
        Log.d(tag, "Process login response: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());
            String access_token = json.getString(JSON_KEY_ACCESS_TOKEN);
            Log.i(tag, "Login success. Get access token: " + access_token);
            ProfileUtil.saveLoginAccessToken(context, access_token);
            return true;
        } catch (Exception e) {
            Log.e(tag, "Cannot convert response to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private Object executeHttpPost(String url, JSONObject data) {
        Object response = null;
        Log.d(tag, "executeHttpPost, url: " + url + ", data" + data);

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(data.toString());
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpost, responseHandler);
            Log.i(tag, "Post success, raw response: " + response);
        } catch (Exception e) {
            Log.e(tag, "Post fail: " + e.toString());
        }

        return response;
    }

}
