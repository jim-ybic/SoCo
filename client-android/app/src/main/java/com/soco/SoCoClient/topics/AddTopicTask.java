package com.soco.SoCoClient.topics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class AddTopicTask extends AsyncTask<String, Void, Boolean> {

    private static final String tag = "AddTopicTask";
    private static final int TIME_OUT = 20*10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS="1";
    public static final String FAILURE="0";

    static final int HTTP_SUCCESS_CODE = 200;

    static final String BOUNDARY = UUID.randomUUID().toString();
    static final String PREFIX = "--";
    static final String LINE_END = "\r\n";
    static final String CONTENT_TYPE = "multipart/form-data"; //内容类型

//    String topicTitle;
//    String topicDesc;
    Topic topic;
    TaskCallBack callback;

//    Context context;
//    SocoApp socoApp;
//    String requestURL;
//    String comment;


    public AddTopicTask(
//            Context context, ContentResolver cr,
//            Uri uriFile, String comment, String eventId, String topicId,
            Topic topic,
            TaskCallBack cb){

//        this.context = context;
//        this.socoApp = (SocoApp) context;
//        this.requestURL = UrlUtil.getTopicUrl();

//        this.cr = cr;
//        this.uriFile = uriFile;
//        this.comment = comment;
//        this.eventId = eventId;
//        this.topicId = topicId;
//        this.topicTitle = topicTitle;
//        this.topicDesc = topicDesc;
        this.topic = topic;
        this.callback = cb;
    }

    protected Boolean doInBackground(String... params) {
        Log.d(tag, "task begin");
        if(SocoApp.user_id == null || SocoApp.user_id.isEmpty()
                || SocoApp.token == null || SocoApp.token.isEmpty()){
            Log.e(tag, "user id or token is not available;");
            return false;
        }

        return uploadFile(SocoApp.user_id, SocoApp.token);
    }

    private Boolean uploadFile(String user_id, String token) {
        try {
            HttpURLConnection conn = initConnection();
            OutputStream outputSteam=conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);

            addUserid(user_id, token, dos);
            addTopicTitle(dos);
            addTopicDesc(dos);

            writeEnddata(dos);

            int res = conn.getResponseCode();
            Log.d(tag, "response code:" + res);
            if(res == HTTP_SUCCESS_CODE) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.toString());
        }
        Log.e(tag, "return failure");
        return false;
    }

    private HttpURLConnection initConnection() throws IOException {
        String requestURL = UrlUtil.getTopicUrl();
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIME_OUT);
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Charset", CHARSET);
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        return conn;
    }

    private void addUserid(String user_id, String token, DataOutputStream dos) throws IOException {
        Log.v(tag, "add userid and token: " + user_id + ", " + token);
        StringBuffer sb = new StringBuffer();

        String name = "user_id";
        String value = user_id;
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        name = "token";
        value = token;
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        Log.v(tag, "dos write userid: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addTopicTitle(DataOutputStream dos) throws IOException {
        Log.v(tag, "add topic title: " + topic.getTitle());
        StringBuffer sb = new StringBuffer();
        String name = "title";
        String value = topic.getTitle();
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(value);
        sb.append(LINE_END);
        Log.v(tag, "dos write text: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addTopicDesc(DataOutputStream dos) throws IOException {
        Log.v(tag, "add topic desc: " + topic.getIntroduction());
        StringBuffer sb = new StringBuffer();
        String name = "introduction";
        String value = topic.getIntroduction();
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(value);
        sb.append(LINE_END);
        Log.v(tag, "dos write text: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void writeEnddata(DataOutputStream dos) throws  IOException {
        Log.v(tag, "write ending done");
        byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();

        Log.v(tag, "write complete");
    }

    protected void onPostExecute(Boolean result){
        Log.v(tag, "post execute: " + result);
        if(result)
            callback.doneTask(true);
        else
            callback.doneTask(false);
    }

}
