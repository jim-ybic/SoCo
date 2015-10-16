package com.soco.SoCoClient.common.util;


import android.util.Log;

import com.soco.SoCoClient.common.http.JsonKeys;

import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;

public class JsonSimulator {

    static final String tag = "JsonSimulator";

    public static JSONObject RegisterSuccessResponse(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonKeys.STATUS, "200");
            obj.put(JsonKeys.USER_ID, "1100101444964821472");
            obj.put(JsonKeys.TOKEN, "D4A83FA835CA20114B5182EF699C87E7F5748B7A7F52DEB47D15707753C521D9");
        } catch (JSONException e) {
            Log.e(tag, "cannot create json data");
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject LoginNormalSuccessResponse(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonKeys.STATUS, "200");
            obj.put(JsonKeys.USER_ID, "1100101444964821472");
            obj.put(JsonKeys.TOKEN, "D4A83FA835CA20114B5182EF699C87E7F5748B7A7F52DEB47D15707753C521D9");
        } catch (JSONException e) {
            Log.e(tag, "cannot create json data");
            e.printStackTrace();
        }
        return obj;
    }
}
