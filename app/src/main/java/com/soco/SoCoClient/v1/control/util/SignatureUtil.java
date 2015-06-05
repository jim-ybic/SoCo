package com.soco.SoCoClient.v1.control.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

import com.soco.SoCoClient.v1.model.Activity;

public class SignatureUtil {

    public static String tag = "SignatureUtil";
    public static String SIGNATURE_KEY = "SocoSig";

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes)
            formatter.format("%02x", b);
        return formatter.toString();
    }

    public static String genSHA1(String data, String key) {
        Log.v(tag, "Generate SHA1 signature of data: " + data + ", key: " + key);
        String s = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            s = toHexString(mac.doFinal(data.getBytes()));
            Log.v(tag, "Signature generated: " + s);
        } catch (Exception e) {
            Log.e(tag, "Error generating SHA1 signature");
            e.printStackTrace();
        }
        return s;
    }

    public static String genSHA1(Activity p){
        String data = p.pid + ", " + p.pname + ", "
                        + p.pcreate_timestamp + ", " + p.pupdate_timestamp;
        String s = genSHA1(data, SIGNATURE_KEY);
        Log.v(tag, "Gen SHA1 signature for project " + p.pname + " is: " + s);
        return s;
    }

    public static String now() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(new Date());
        return s;
    }

    public static String genSharedFileSummary(ArrayList<String> sharedFileDisplayName) {
        String s = new String();
        for(String n : sharedFileDisplayName){
            if (s.isEmpty())
                s = n;
            else
                s += "\n" + n;
        }
        return s;
    }
}