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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String cookie = null;
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        httpPost.setHeader("Content-Type", "application/json");
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            HttpEntity entity = response.getEntity();
            Header cookieHeader = response.getFirstHeader("Set-Cookie");
            cookie = cookieHeader.getValue().split(";")[0];
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return fetchRequestWithCookie(cookie);
    }

    public static String fetchRequestWithCookie(String cookie) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", cookie);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return responseBody;
        } finally {
            response.close();
        }
    }
}
