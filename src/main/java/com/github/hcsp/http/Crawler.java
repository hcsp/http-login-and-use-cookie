package com.github.hcsp.http;


import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        String cookie = sendHttpPost("http://47.91.156.35:8000/auth/login", json);
        return sendHttpGet("http://47.91.156.35:8000/auth", cookie);
    }

    public static String sendHttpPost(String url, String json) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            httpPost.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response1 = httpclient.execute(httpPost)) {
                HttpEntity entity1 = response1.getEntity();
                String cookie = response1.getFirstHeader("Set-Cookie").getValue();
                EntityUtils.consume(entity1);
                return cookie.replaceAll(";.*", "");
            }
        }
    }

    public static String sendHttpGet(String url, String cookie) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            httpGet.addHeader("Cookie", cookie);
            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
                HttpEntity entity1 = response1.getEntity();
                InputStream is = entity1.getContent();
                String html = IOUtils.toString(is, StandardCharsets.UTF_8);
                System.out.println(html);
                EntityUtils.consume(entity1);
                return html;
            }
        }
    }
}
