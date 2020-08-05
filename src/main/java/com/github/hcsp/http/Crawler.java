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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = cratePostAndSetHeader(username, password);
        CloseableHttpResponse postResponse = httpclient.execute(httpPost);
        String cookie = getCookie(postResponse);
        try {
            HttpEntity entity = postResponse.getEntity();
            EntityUtils.consume(entity);
        } finally {
            postResponse.close();
        }
        HttpGet httpGet = crateGetAndSetHeader(cookie);
        CloseableHttpResponse getResponse = httpclient.execute(httpGet);
        try {
            HttpEntity entity = getResponse.getEntity();
            InputStream inputStream = entity.getContent();
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
        } finally {
            getResponse.close();
        }
        return result;
    }

    private static HttpPost cratePostAndSetHeader(String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpPost.addHeader("Content-Type", " application/json;charset=UTF-8");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonString = JSON.toJSONString(map);
        StringEntity se = new StringEntity(jsonString);
        httpPost.setEntity(se);
        return httpPost;
    }

    private static String getCookie(CloseableHttpResponse response) {
        String rawCookie = response.getFirstHeader("Set-Cookie").getValue();
        return rawCookie.split(";")[0];
    }

    private static HttpGet crateGetAndSetHeader(String cookie) {
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpGet.addHeader("Content-Type", " application/json;charset=UTF-8");
        httpGet.addHeader("Cookie", cookie);
        return httpGet;
    }
}




