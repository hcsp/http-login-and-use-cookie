package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        httpPost.setHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
        String json = JSON.toJSONString(map);
        HttpEntity entity1 = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        httpPost.setEntity(entity1);
        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        String cookie = response1.getFirstHeader("Set-Cookie").getValue().split(";")[0];
//        System.out.println(cookie);

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        System.out.println(response2.getStatusLine());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        httpGet.setHeader("Cookie", cookie);
        System.out.println(response2);
        HttpEntity entity2 = response2.getEntity();
        String html = IOUtils.toString(entity2.getContent(), StandardCharsets.UTF_8);
        return html;
    }
}
