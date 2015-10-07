package com.soco.security.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*******************************************************************************
 * AES
 * 
 * from internet
 * 算法/模式/填充                               16字节加密后数据长度                       不满16字节加密后长度
    AES/CBC/NoPadding             16                         不支持
    AES/CBC/PKCS5Padding          32                         16
    AES/CBC/ISO10126Padding       32                          16
    AES/CFB/NoPadding             16                          原始数据长度
    AES/CFB/PKCS5Padding          32                          16
    AES/CFB/ISO10126Padding       32                          16
    AES/ECB/NoPadding             16                          不支持
    AES/ECB/PKCS5Padding          32                          16
    AES/ECB/ISO10126Padding       32                          16
    AES/OFB/NoPadding             16                          原始数据长度
    AES/OFB/PKCS5Padding          32                          16
    AES/OFB/ISO10126Padding       32                          16
    AES/PCBC/NoPadding            16                          不支持
    AES/PCBC/PKCS5Padding         32                          16
    AES/PCBC/ISO10126Padding      32                          16
 */
 
public class AES {
 
    // encrypt
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key is null");
            return null;
        }
        // Key is 16
        if (sKey.length() != 16) {
            System.out.print("Key should be 16 bytes");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
         
        //System.out.println("TEXT = " + new String(encrypted));
        return byte2hex(encrypted);
    }
 
    // decrypt
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // check Key
            if (sKey == null) {
                System.out.print("Key is null");
                return null;
            }
            // key should be 16 bytes
            if (sKey.length() != 16) {
                System.out.print("Key should be 16 bytes");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    public static String getRandomSecKey(){
    	String key = "";

		final SecureRandom prng = new SecureRandom();
		final byte[] aes128KeyData  = new byte[64 / Byte.SIZE];
		prng.nextBytes(aes128KeyData);
		final SecretKey tmp = new SecretKeySpec(aes128KeyData, "AES");
		
		key =toHex(tmp.getEncoded());

        return key;
    }
    
    private static String toHex(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (final byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
 
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }
 
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
 
    /*
     * this main function is for testing
     * */
    public static void main(String[] args) throws Exception {
        /*
         *  the Key could consist of numbers and letters which length should be 26 bytes，no padding
         */
        String cKey = getRandomSecKey();
        // the encrypted string
        String cSrc = "1111122222333334445";
        System.out.println(cSrc);
        // encrypt
        long lStart = System.currentTimeMillis();
        String enString = AES.Encrypt(cSrc, cKey);
        System.out.println("the encrypted string：" + enString);
 
        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("spend：" + lUseTime + " millionseconds");
        // decrypt
        lStart = System.currentTimeMillis();
        String DeString = AES.Decrypt(enString, cKey);
        System.out.println("the decrypted string：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("spend：" + lUseTime + " millionseconds");
        ////
        Date d = new Date();
        long m = d.getTime() + 2592000000l;
        String nd = d.toString();
        Date dd = new Date(m);
        String nd1 = dd.toString();
        System.out.println("now: " + nd);
        System.out.println("after 1 month: " + nd1);
    }
}
