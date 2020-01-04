package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static CloseableHttpClient client = HttpClients.createDefault();

    public static String loginAndGetResponse(String username, String password) throws IOException {
        // 将用户信息转换成json字节流
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String loginInfoTOJson = JSON.toJSONString(map);
        return loginUsingPost(loginInfoTOJson);
    }

    public static String loginUsingPost(String loginInfomation) throws IOException {
        HttpPost post = new HttpPost("http://47.91.156.35:8000/auth/login");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        post.setEntity(new StringEntity(loginInfomation));
        CloseableHttpResponse response = client.execute(post);
        return getCookie(response);
    }

    public static String getCookie(CloseableHttpResponse response) throws IOException {
        String resultCookie;
        try {
            System.out.println(response.getStatusLine());
            resultCookie = response.getHeaders("Set-Cookie")[0].getValue();    // 获得 Cookie
        } finally {
            response.close();
        }
        String getCookie = resultCookie.split(";")[0];
        return loginUsingCookie(getCookie);
    }

    public static String loginUsingCookie(String cookie) throws IOException {
        HttpGet get = new HttpGet("http://47.91.156.35:8000/auth");
        get.addHeader("Content-Type", "application/json");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        get.addHeader("Cookie", cookie);
        CloseableHttpResponse response = client.execute(get);
        String responseString;
        try {
            System.out.println(response.getStatusLine());
            responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } finally {
            response.close();
        }
        return responseString;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}
