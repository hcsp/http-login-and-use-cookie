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
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
        String user = JSON.toJSONString(map);

        httpPost.setEntity(new StringEntity(user, "utf-8"));
        CloseableHttpResponse resp = httpclient.execute(httpPost);
        String cookie = resp.getFirstHeader("Set-Cookie").getValue().split(";")[0];

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        HttpEntity entity1 = response1.getEntity();
        String body = EntityUtils.toString(entity1, "utf-8");
        return body;
    }
}
