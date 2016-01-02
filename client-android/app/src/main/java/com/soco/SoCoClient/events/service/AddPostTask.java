package com.soco.SoCoClient.events.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

public class AddPostTask extends AsyncTask<String, Void, Boolean> {

    private static final String tag = "AddPostTask";
    private static final int TIME_OUT = 20*10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS="1";
    public static final String FAILURE="0";

    static final int HTTP_SUCCESS_CODE = 200;

    static final String BOUNDARY = UUID.randomUUID().toString();
    static final String PREFIX = "--";
    static final String LINE_END = "\r\n";
    static final String CONTENT_TYPE = "multipart/form-data"; //内容类型

    Context context;
    SocoApp socoApp;
    File file;
    String requestURL;
    ContentResolver cr;
    Uri uriFile;
    String comment;
    String eventId;
    String suffix;
    TaskCallBack callback;
    String imageType = "image";

    public AddPostTask(
            Context context, ContentResolver cr,
            Uri uriFile, String comment, String eventId,
            TaskCallBack cb){

        this.context = context;
        this.socoApp = (SocoApp) context;
        this.requestURL = UrlUtil.getEventCommentUrl();

        this.cr = cr;
        this.uriFile = uriFile;
        this.comment = comment;
        this.eventId = eventId;
        this.callback = cb;

        try {
            if(uriFile != null)
                this.file = new File(uriFile.getPath());

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = cr.query(uriFile, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                Log.d(tag, "filepath: " + filePath);
                this.suffix = filePath.substring(filePath.lastIndexOf("."));  //e.g. .jpg
            }
            cursor.close();
        }
        catch (Exception e){
            Log.e(tag, "file error: " + e.toString());
            e.printStackTrace();
        }
    }

    protected Boolean doInBackground(String... params) {
        Log.d(tag, "task begin");
        if(socoApp.user_id == null || socoApp.user_id.isEmpty()
                || socoApp.token == null || socoApp.token.isEmpty()){
            Log.e(tag, "user id or token is not available;");
            return false;
        }

        return uploadFile(socoApp.user_id, socoApp.token);
    }

    private Boolean uploadFile(String user_id, String token) {
        try {
            HttpURLConnection conn = initConnection();
            OutputStream outputSteam=conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);

            addUserid(user_id, token, dos);
            addEventId(dos);
            addText(dos);
            addImageType(dos);
            addFile(dos);

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
        String value = user_id;     //"1100101446780892087";
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        name = "token";
        value = token;  //"CD03F5726948EC3C43DFEAFD8E60B89A384085F8BC4132CC02618D616739F353";
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        Log.v(tag, "dos write userid: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addEventId(DataOutputStream dos) throws IOException {
        Log.v(tag, "add eventid: " + eventId);
        StringBuffer sb = new StringBuffer();

        String name = "event_id";
        String value = eventId;
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        Log.v(tag, "dos write eventid: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addText(DataOutputStream dos) throws IOException {
        Log.v(tag, "add text: " + comment);
        StringBuffer sb = new StringBuffer();

        String name = "text";
        String value = comment;
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        Log.v(tag, "dos write text: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addImageType(DataOutputStream dos) throws IOException {
        Log.v(tag, "add imagetye: " + imageType);
        StringBuffer sb = new StringBuffer();

        String name = "image_type";
        String value = imageType;
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        sb.append(LINE_END);
        sb.append(URLEncoder.encode(value, "UTF-8") + LINE_END);

        Log.v(tag, "dos write imagetype: " + sb.toString());
        dos.write(sb.toString().getBytes());
    }

    private void addFile(DataOutputStream dos) throws IOException {
        if(file == null) {
            Log.v(tag, "no file to upload");
            return;
        }

        Log.v(tag, "write meta data");
        StringBuffer sb = new StringBuffer();

        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"filename\"; filename=\"" + file.getName() + suffix + "\"");
        sb.append(LINE_END);
        sb.append("Content-Type: application/octet-stream; charset=" + CHARSET);
        sb.append(LINE_END);
        sb.append(LINE_END);
        dos.write(sb.toString().getBytes());

        Log.v(tag, "write file bytes");
        InputStream is = cr.openInputStream(uriFile);
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len=is.read(bytes))!=-1)
        {
            dos.write(bytes, 0, len);
        }
        is.close();
        dos.write(LINE_END.getBytes());

        return;
    }

    private void writeEnddata(DataOutputStream dos) throws  IOException {
        Log.v(tag, "write ending done");
        byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();

        Log.v(tag, "write complete");
    }

    protected void onPostExecute(Boolean result){
        Log.v(tag, "post execute");
        if(result)
            callback.doneTask(true);
        else
            callback.doneTask(false);
    }

}
