package com.github.hcsp.http;


import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36\\n\"");

        Map<String, String> map = new HashMap<>();
        map.put(username, password);
        map.put(username, password);
        String text = JSON.toJSONString(map);
        StringEntity entity = new StringEntity(text);
        httpPost.setEntity(entity);

        CloseableHttpResponse responsePost = httpclient.execute(httpPost);
        responsePost.close();

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", "JSESSIONID=26357FA9869B0AB4E3851237E0EE576A; Path=/; HttpOnly");

        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        responseGet.close();

        System.out.println(responseGet);
        return String.valueOf(responseGet);
    }
}
