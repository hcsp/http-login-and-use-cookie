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

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        StringEntity text = new StringEntity(JSON.toJSONString(map));
        httpPost.setEntity(text);

        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        String cookie = response1.getFirstHeader("Set-Cookie").getValue().split(";")[0];

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        HttpEntity entity = response2.getEntity();
        InputStream html = entity.getContent();

        return IOUtils.toString(html, StandardCharsets.UTF_8);
    }
//    public static void main(String[] args) throws IOException {
//        loginAndGetResponse("xdml","xdml");
//    }

}
