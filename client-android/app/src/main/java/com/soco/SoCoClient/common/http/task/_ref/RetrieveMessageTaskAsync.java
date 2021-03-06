package com.soco.SoCoClient.common.http.task._ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.DataConfigV1;
import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient._ref.HttpUtilV1;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.model.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
public class RetrieveMessageTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "RetrieveMessageTask";

    String url;
    Context context;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;

    public RetrieveMessageTaskAsync(
            String url,
            Context context
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.url = url;
        this.context = context;

        socoApp = (SocoApp)context;
        profile = socoApp.profile;
        dbManagerSoco = socoApp.dbManagerSoco;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() ){
            Log.e(tag, "Cannot get url");
            return false;
        }

        execute(url, context);
        return true;
    }

    public void execute(String url, Context context){
        Object response = request(url);
        if (response != null)
            parse(response, context);
    }

    public Object request(String url) {
        JSONObject data = new JSONObject();
        //no data needed for retrieve message
        return HttpUtilV1.executeHttpPost(url, data);
    }

    /*
    Sample response - success:
        {status:"success",
        message:[
            {from_type:1, from_id:"test@test.com", to_type:2, to_id:1,
            send_date_time:"2015-04-05 12:12:12", content_type:1, signature:"XYZABC123",
            email:"hi how are you"},
            {another message}
        ],
        finish:1}
    Sample response - failure:
        {status:"failure"}
    */
    public boolean parse(Object response, Context context) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server response status success");

                //extract messages
                JSONArray messageArray = new JSONArray(
                        json.getString(HttpConfigV1.JSON_KEY_MESSAGE));

                //process each message
                for (int i = 0; i < messageArray.length(); i++) {
                    JSONObject message = messageArray.getJSONObject(i);

                    int from_type = message.getInt(HttpConfigV1.JSON_KEY_FROM_TYPE);
                    String from_id = message.getString(HttpConfigV1.JSON_KEY_FROM_ID);
                    Log.i(tag, "Get message, from: " + from_type + ", " + from_id);

                    int to_type = message.getInt(HttpConfigV1.JSON_KEY_TO_TYPE);
                    String to_id = message.getString(HttpConfigV1.JSON_KEY_TO_ID);
                    Log.i(tag, "Get message, to: "   + to_type + ", " + to_id);

                    String send_date_time = message.getString(HttpConfigV1.JSON_KEY_SEND_DATE_TIME);
                    Log.i(tag, "Get message, timestamp: " + send_date_time);

                    String signature = message.getString(HttpConfigV1.JSON_KEY_SIGNATURE);
                    Log.i(tag, "Get message, signature: " + signature);

                    int content_type = message.getInt(HttpConfigV1.JSON_KEY_CONTENT_TYPE);
                    String content = message.getString(HttpConfigV1.JSON_KEY_CONTENT);
                    Log.i(tag, "Get message, email: " + content_type + ", " + content);

                    Log.d(tag, "save message into database, msg type is " + to_type);
                    if(to_type == HttpConfigV1.MESSAGE_TO_TYPE_2) {       //type 2: send to activity
                        int aid_local = dbManagerSoco.findLocalAidByServerAid(Integer.parseInt(to_id));
                        if(aid_local == -1)
                            Log.e(tag, "cannot find local activity with remote id " + to_id);
                        Log.i(tag, "add comment to project: " + aid_local + ", " + content
                                + ", " + from_id);
                        dbManagerSoco.addCommentToProject(content, aid_local, from_id);
                    }
                    else if(to_type == HttpConfigV1.MESSAGE_TO_TYPE_1){   //type 1: send to member
                        Log.i(tag, "save message into database: " + message + ", from " +from_id);

                        //todo: need to get contactId for a given contact email (from_id)
                        int contactId = 4;  //todo: for testing only
                        dbManagerSoco.addMessage(
                                contactId, content, DataConfigV1.CHAT_TYPE_RECEIVE);
                    }

                    Log.d(tag, "send ack to server");
                    String url = UrlUtil.getAckRetrieveMessageUrl(context);
                    AckRetrieveMessageTaskAsync task = new AckRetrieveMessageTaskAsync(
                            url, signature);
                    task.execute();
                }
            }
            else {
                Log.e(tag, "Parse result not in success status");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
