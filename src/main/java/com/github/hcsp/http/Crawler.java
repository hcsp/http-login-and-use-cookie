package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

public class Crawler {
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static String login(String username, String password) throws IOException {
        String cookie = "";
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        HashMap<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        StringEntity requestBodyString = new StringEntity(JSON.toJSONString(requestBodyMap));
        httpPost.setEntity(requestBodyString);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");

        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            String cookieValue = response.getHeaders("set-cookie")[0].getValue();
            cookie = cookieValue.split(";")[0];
        } finally {
            response.close();
        }

        return cookie;
    }

    public static String getResponse(String cookie) throws IOException {
        String responseBodyString;

        try {
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.addHeader("Cookie", cookie);

            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            responseBodyString = EntityUtils.toString(entity);
        } finally {
            httpClient.close();
        }

        return responseBodyString;
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String responseBodyString;

        String cookie = login(username, password);
        responseBodyString = getResponse(cookie);

        return responseBodyString;
    }
}
