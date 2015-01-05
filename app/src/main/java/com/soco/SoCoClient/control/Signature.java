package com.soco.SoCoClient.control;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;
import android.util.Xml.Encoding;


/**
 * This class defines common routines for generating
 * authentication signatures for AWS requests.
 */
public class Signature {

    //TEST1
//    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";



    /**
     * Computes RFC 2104-compliant HMAC signature.
     * * @param data
     * The data to be signed.
     * @param key
     * The signing key.
     * @return
     * The Base64-encoded RFC 2104-compliant HMAC signature.
     * @throws
     * java.security.SignatureException when signature generation fails
     */
    public static String calculateRFC2104HMAC(String data, String key)
            throws java.security.SignatureException
    {
        Log.i("key", "Gen signature of data: " + data + ", key: " + key);
        String result;
        try {

// get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

// get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

// compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

// base64-encode the hmac
//            result = Encoding.EncodeBase64(rawHmac);
            result = new String(rawHmac);
            Log.i("key", "Signature generated: " + result);

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    //TEST2
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public static String calculateRFC2104HMAC2(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        Log.i("key", "Gen signature of data: " + data + ", key: " + key);

        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        String s = toHexString(mac.doFinal(data.getBytes()));
        Log.i("key", "Signature generated: " + s);

        return s;
    }

    public static void main(String[] args) throws Exception {
        String hmac = calculateRFC2104HMAC("data", "key");

//        System.out.println(hmac);
        Log.i("key", "Signature generated: " + hmac);

//        assert hmac.equals("104152c5bfdca07bc633eebd46199f0255c9f49d");
    }
}