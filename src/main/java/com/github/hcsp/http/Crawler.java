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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String url = "http://47.91.156.35:8000/auth/login";
    private static final String url2 = "http://47.91.156.35:8000/auth";
    private static final String content_type_value = "application/json";
    private static final String user_agent_value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", content_type_value);
        httpPost.setHeader("User-Agent", user_agent_value);


        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));

        CloseableHttpResponse response1 = httpclient.execute(httpPost);
        try {
            String cookie = response1.getFirstHeader("Set-Cookie").getValue();
            return GetJson(cookie);
        } finally {
            response1.close();
        }
    }

    public static String GetJson(String cookie_value) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url2);
        httpGet.setHeader("Content-Type", content_type_value);
        httpGet.setHeader("User-Agent", user_agent_value);
        httpGet.setHeader("Cookie", cookie_value);

        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            HttpEntity entity1 = response1.getEntity();

            InputStream is = entity1.getContent();

            String str = IOUtils.toString(is, "UTF-8");

            return str;
        }
    }

}
