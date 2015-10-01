package com.soco.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.soco.log.Log;

public class Utility {

    public static String getCurrentDateToString(){
        String dateStr = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date current = new Date();
        dateStr = sdf.format(current);
        return dateStr;
    }
    
    /**
     * At server side, use ZipOutputStream to zip text to byte array, then convert
     * byte array to base64 string, so it can be trasnfered via http request.
     */
    public static String compressString(String srcTxt) {
        String str = null;
        try{
            ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(rstBao);
            zos.write(srcTxt.getBytes());
            IOUtils.closeQuietly(zos);
            byte[] bytes = rstBao.toByteArray();  
            str = Base64.encodeBase64String(bytes);
        }catch(IOException e){
            Log.infor(e.getMessage());
            str = "";
        }
        return str;
    }
    
    /**
     * When client receives the zipped base64 string, it first decode base64
     * String to byte array, then use ZipInputStream to revert the byte array to a
     * string.
     */
    public static String uncompressString(String zippedBase64Str)  {
        String result = null;
        GZIPInputStream zi = null;
        try {
            byte[] bytes = Base64.decodeBase64(zippedBase64Str);
            zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
            result = IOUtils.toString(zi);
        } catch(IOException e){
            Log.infor(e.getMessage());
            result = "";
        } finally {
            IOUtils.closeQuietly(zi);
        }
        return result;
    }
}
