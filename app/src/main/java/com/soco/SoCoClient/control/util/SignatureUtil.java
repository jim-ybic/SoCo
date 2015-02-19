package com.soco.SoCoClient.control.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.model.Project;

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
        Log.d(tag, "Generate SHA1 signature of data: " + data + ", key: " + key);
        String s = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            s = toHexString(mac.doFinal(data.getBytes()));
            Log.d(tag, "Signature generated: " + s);
        } catch (Exception e) {
            Log.e(tag, "Error generating SHA1 signature");
            e.printStackTrace();
        }
        return s;
    }

    public static String genSHA1(Project p){
        String data = p.pid + ", " + p.pname + ", "
                        + p.pcreate_timestamp + ", " + p.pupdate_timestamp;
        String s = genSHA1(data, SIGNATURE_KEY);
        Log.i(tag, "Gen SHA1 signature for project " + p.pname + " is: " + s);
        return s;
    }

    public static String now() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(new Date());
        return s;
    }

    public static String genSharedFileSummary(ArrayList<HashMap<String, String>> attrMap){
        Log.i(tag, "Gen shared file summary start, attribute size: " + attrMap.size());
        String s = new String();
        for(HashMap<String, String> map : attrMap) {
            for (HashMap.Entry<String, String> e : map.entrySet()) {
                Log.d(tag, "Current attribute key: " + e.getKey());
                if (e.getKey().equals(DataConfig.ATTRIBUTE_NAME_FILE_REMOTE_PATH)) {
                    String remotePath = e.getValue();
                    int pos = remotePath.lastIndexOf("/");
                    String displayName = remotePath.substring(pos + 1, remotePath.length());
                    Log.i(tag, "Found remote path: " + remotePath + ", "
                            + " display name: " + displayName);
                    if (s.isEmpty())
                        s = displayName;
                    else
                        s += ", " + displayName;
                }
            }
        }
        return s;
    }

}