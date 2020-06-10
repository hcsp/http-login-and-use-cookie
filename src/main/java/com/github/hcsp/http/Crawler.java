package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Crawler {
    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        String cookie = "";

        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
        httpPost.setHeader("content-type", "application/json");
        httpPost.setEntity(new StringEntity(parsePostDataToJsonString(username, password)));

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            Header setCookie = response.getFirstHeader("Set-Cookie");
            cookie = setCookie.getValue().split(";")[0];
        }
        return getResponseWithCookie(cookie);
    }

    public static String getResponseWithCookie(String cookie) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");

        httpGet.addHeader("Cookie", cookie);

        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity1 = response.getEntity();
            String responseToString = IOUtils.toString(entity1.getContent(), StandardCharsets.UTF_8);
            return responseToString;
        }
    }

    public static String parsePostDataToJsonString(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }
}
