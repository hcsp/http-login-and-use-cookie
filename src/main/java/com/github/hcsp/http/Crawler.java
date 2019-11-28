package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static StringEntity stringEntity;
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static CloseableHttpResponse response = null;

    public static String userToJsonString(String username,String password){
        Map<String, String> map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }
    public static void postResponse(String url,String json){
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");

        try {
            stringEntity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(stringEntity);

        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getResponse(String url){
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie", getCookie());

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCookie(){
        Header[] httpHeaders = response.getAllHeaders();
        String cookie = null;
        for (Header i : httpHeaders) {
            if ("Set-Cookie".equals(i.getName())) {
                cookie = i.getValue();
                break;
            }
        }
        cookie = cookie.split(";")[0];
        return cookie;
    }

    public static String getResultString(){
        String result = null;
        HttpEntity entity = response.getEntity();
        try {
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String loginAndGetResponse(String username, String password) {
        String url1 = "http://47.91.156.35:8000/auth/login";
        String url2 = "http://47.91.156.35:8000/auth";

        // USER_TO_JSON
        String json = userToJsonString(username, password);

        // POST
        postResponse(url1,json);

        // GET
        getResponse(url2);

        return getResultString();
    }
}









