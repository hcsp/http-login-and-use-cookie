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
    public static String loginAndGetResponse(String username, String password) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        StringEntity stringEntity = null;
        String result = null;
        String url1 = "http://47.91.156.35:8000/auth/login";
        String url2 = "http://47.91.156.35:8000/auth";

        // post 请求
        HttpPost httpPost = new HttpPost(url1);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");

        Map<String, String> map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);

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

        // get 请求
        Header[] httpHeaders = response.getAllHeaders();
        String cookie = null;
        for (Header i : httpHeaders) {
            if ("Set-Cookie".equals(i.getName())) {
                cookie = i.getValue();
                break;
            }
        }

        cookie = cookie.split(";")[0];

        HttpGet httpGet = new HttpGet(url2);
        httpGet.addHeader("Cookie", cookie);

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        try {
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}









