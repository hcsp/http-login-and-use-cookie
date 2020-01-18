package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, Object> usermap = new HashMap<>();
        usermap.put("username", username);
        usermap.put("password", password);
        String cookie = getResponseCookie(usermap);
        return withCookieRequsetBody(cookie);
    }

    private static String withCookieRequsetBody(String cookie) throws IOException {
        CloseableHttpClient httpclient2 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        addhttpGetHeader(cookie, httpGet);
        CloseableHttpResponse response2 = httpclient2.execute(httpGet);
        System.out.println("secoundRequest:" + response2.getStatusLine());
        String havecookieJson;
        try {
            HttpEntity entity = response2.getEntity();
            InputStream is = entity.getContent();
            havecookieJson = IOUtils.toString(is, StandardCharsets.UTF_8);
        } finally {
            response2.close();
        }
        return havecookieJson;
    }

    private static String getResponseCookie(Map<String, Object> usermap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        addhttpPostHeaderAndEntity(usermap, httpPost);
        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        System.out.println("firstRequest:" + response1.getStatusLine());
        StringBuilder cookie;
        try {
            cookie = getCookie(response1);
            System.out.println("cookie:" + cookie);
        } finally {
            response1.close();
        }
        return cookie.toString();
    }

    private static void addhttpGetHeader(String cookie, HttpGet httpGet) {
        httpGet.addHeader("Cookie", cookie);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
    }

    private static StringBuilder getCookie(CloseableHttpResponse response1) {
        Header[] headers = response1.getHeaders("Set-Cookie");
        StringBuilder cookie = new StringBuilder();
        for (Header h : headers) {
            cookie.append(h.getValue());
        }
        return cookie;
    }

    private static void addhttpPostHeaderAndEntity(Map<String, Object> userMap, HttpPost httpPost) throws UnsupportedEncodingException {
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(userMap)));
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
    }

}
