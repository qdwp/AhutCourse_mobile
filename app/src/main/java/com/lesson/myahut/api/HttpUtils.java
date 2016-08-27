package com.lesson.myahut.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by qidunwei on 2016/1/10.
 * version 1.1
 */
public class HttpUtils {

    private static final Boolean DOINPUT = true;
    private static final Boolean DOOUTPUT = true;
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final int CONNECTTIMEOUT = 5000;
    private static final int READTIMEOUT = 5000;

    /**
     * 向服务器发送 GET 请求
     *
     * @param urlString 指定功能 URL
     * @param params    POST参数
     */
    public static String doGetMethod(String urlString, String params) {
        HttpURLConnection connection = null;
        String result = null;
        String _URL = urlString;
        if (params != null) {
            _URL += "?" + params;
        }
        try {
            URL url = new URL(_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_METHOD);
            connection.setConnectTimeout(CONNECTTIMEOUT);
            connection.setReadTimeout(READTIMEOUT);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            if (connection.getResponseCode() == 200) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                result = response.toString();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }


    /**
     * 向服务器发送 POST 请求
     *
     * @param urlString 指定功能 URL
     * @param params    POST参数
     */
    public static String doPostMethod(String urlString, String params) {
        HttpURLConnection connection = null;
        String result = null;
        String _URL = urlString;
        try {
            URL url = new URL(_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(POST_METHOD);
            connection.setConnectTimeout(CONNECTTIMEOUT);
            connection.setReadTimeout(READTIMEOUT);
            connection.setDoInput(DOINPUT);
            connection.setDoOutput(DOOUTPUT);

                    /* post json格式的字符串数据时，必须加上这个Header，否则返回 500 */
            connection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                result = response.toString();
            }
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}
