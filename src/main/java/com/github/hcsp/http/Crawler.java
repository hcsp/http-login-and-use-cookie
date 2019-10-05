package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    private static final String UTF_8 = "UTF-8";
    private static final String LOGIN_URL = "http://47.91.156.35:8000/auth/login";
    private static final String TEST_AUTH = "http://47.91.156.35:8000/auth";


    public static String loginAndGetResponse(String username, String password) throws IOException {

        //通过CookieStore保持上下文的回话
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(cookieStore).build();

        HttpPost httpPost = new HttpPost(LOGIN_URL);

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonString = JSON.toJSONString(map);

        httpPost.setEntity(new StringEntity(jsonString, UTF_8));
        httpClient.execute(httpPost);

        CloseableHttpResponse execute = httpClient.execute(new HttpGet(TEST_AUTH));
        return EntityUtils.toString(execute.getEntity(), UTF_8);
    }
}
