package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = loginAndGetCookie(username, password);
        System.out.println("cookie = " + cookie);
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Set-Cookie", cookie);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    public static String loginAndGetCookie(String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonStr = JSONObject.toJSONString(map);

        StringEntity data = new StringEntity(jsonStr);
        httpPost.setEntity(data);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return response.getHeaders("Set-Cookie")[0].getValue().split(";")[0].split("=")[1];
    }
}
