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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    static String cookie;

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String request;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");


        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        StringEntity stringEntity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(stringEntity);


        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");

        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();

            InputStream inputStream = httpEntity.getContent();
            request = IOUtils.toString(inputStream, "UTF-8");
            String[] headers = response.getFirstHeader("Set-Cookie").getValue().split(";");
            cookie = headers[0];

        } finally {
            httpclient.close();
        }
        return request;
    }

    public static String HttpGetQuest() throws IOException {
        String result;
        CloseableHttpClient httpclientQuest = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", cookie);
        CloseableHttpResponse response1 = httpclientQuest.execute(httpGet);
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();

            result=IOUtils.toString(entity1.getContent(), "UTF-8");
            System.out.println(result);
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
        return result;
    }
}
