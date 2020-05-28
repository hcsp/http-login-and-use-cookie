package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String LOGIN = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        String cookie = post(json);
        return get(cookie);
    }

    private static String get(String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(AUTH);
        // add Headers
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Cookie", cookie);
        String result;
        try (CloseableHttpResponse getResponse = httpClient.execute(httpGet)) {
            InputStream content = getResponse.getEntity().getContent();
            result = IOUtils.toString(content, StandardCharsets.UTF_8);
        }
        return result;
    }

    private static String post(String json) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(LOGIN);
        // add Headers
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();


        InputStream stream = IOUtils.toInputStream(json, StandardCharsets.UTF_8);
        basicHttpEntity.setContent(stream);

        httpPost.setEntity(basicHttpEntity);

        String result;
        try (CloseableHttpResponse postResponse = httpClient.execute(httpPost)) {
            result = postResponse.getHeaders("Set-Cookie")[0].getValue();
        }
        return result;
    }
}
