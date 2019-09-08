package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).build();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        httpPost.setEntity(new StringEntity(JSON.toJSONString(map), "UTF-8"));
        CloseableHttpResponse response1 = httpClient.execute(httpPost);
        response1.close();


        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        CloseableHttpResponse response2 = httpClient.execute(httpGet);

        HttpEntity httpEntity=response2.getEntity();
        InputStream httpEntityContent = httpEntity.getContent();
        String html = IOUtils.toString(httpEntityContent, "UTF-8");
        response2.close();


        return html;

    }

    public static void main(String[] args) throws IOException {
        System.out.println(loginAndGetResponse("xdml", "xdml"));
    }

}
