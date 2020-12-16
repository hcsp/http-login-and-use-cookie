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
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));

        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        try {
            Header header = response1.getFirstHeader("Set-Cookie");
            HttpEntity entity = response1.getEntity();
            cookie = header.getValue().split(";")[0];
            EntityUtils.consume(entity);
        } finally {
            response1.close();
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36");

        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        try {
            HttpEntity entity2 = response2.getEntity();
            String headerBody = EntityUtils.toString(entity2);
            EntityUtils.consume(entity2);
            return headerBody;
        } finally {
            response2.close();
        }
    }
}