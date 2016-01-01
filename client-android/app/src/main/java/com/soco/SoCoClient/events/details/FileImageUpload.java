package com.soco.SoCoClient.events.details;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FileImageUpload {
    private static final String tag = "FileImageUpload";
    private static final int TIME_OUT = 20*10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS="1";
    public static final String FAILURE="0";
    /** * android上传文件到服务器
     * @param file 需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public static String uploadFile(
            File file,String RequestURL, ContentResolver cr, Uri uri,
            String suffix
    ) {
//        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
        String BOUNDARY = "----------------boundary1234567890";
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", CHARSET);
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            StringBuffer sb = new StringBuffer();
            if(true) {
                Log.d(tag, "add userid and token");

                OutputStream outputSteam=conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                Log.v(tag, "os: " + outputSteam + ", dos: " + dos);

                String name = "user_id";
                String value = "1100101446780892087";
                sb.append("--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + name
                        + "\"\r\n");
                sb.append("\r\n");
                sb.append(URLEncoder.encode(value, "UTF-8") + "\r\n");

                name = "token";
                value = "CD03F5726948EC3C43DFEAFD8E60B89A384085F8BC4132CC02618D616739F353";
                sb.append("--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + name
                        + "\"\r\n");
                sb.append("\r\n");
                sb.append(URLEncoder.encode(value, "UTF-8") + "\r\n");

                Log.v(tag, "sb: " + sb.toString());
            }
            if(file!=null) {
                Log.d(tag, "prepare to upload");
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam=conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                Log.d(tag, "os: " + outputSteam + ", dos: " + dos);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"filename\"; filename=\"" + file.getName() + suffix + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                Log.v(tag, "sb: " + sb.toString());
                dos.write(sb.toString().getBytes());
                Log.d(tag, "write sb done");

//                ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes2);
//                String path = MediaStore.Images.Media.insertImage(cr, bitmap, "Title", null);
//                Uri uri2 = Uri.parse(path);
//                File file2 = new File(uri2.getPath());
//                Log.d(tag, "resized file length: " + file2.length());

//                InputStream is = new FileInputStream(file);
                InputStream is = cr.openInputStream(uri);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                Log.d(tag, "write byte done");
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                Log.d(tag, "write complete");
                /**
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.d(tag, "response code:" + res);
                if(res==200)
                {
                    return SUCCESS;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.toString());
            return FAILURE;
        }
        Log.e(tag, "return failure");
        return FAILURE;
    }
}
