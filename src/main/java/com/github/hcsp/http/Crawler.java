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
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String text = JSON.toJSONString(map);
        StringEntity entity = new StringEntity(text);
        httpPost.setEntity(entity);

        CloseableHttpResponse responsePost = httpclient.execute(httpPost);
        try {
            System.out.println(responsePost.getStatusLine());
            cookie = responsePost.getFirstHeader("Set-Cookie").getValue();
            System.out.println(cookie);
        } finally {
            responsePost.close();
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth/");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        try {
            System.out.println(responseGet.getStatusLine());
            HttpEntity entity1 = responseGet.getEntity();

            InputStream is = entity1.getContent();
            return IOUtils.toString(is, "UTF-8");
        } finally {
            responseGet.close();
        }
    }
}

