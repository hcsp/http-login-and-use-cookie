package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        try {
            return getJson(response2.getFirstHeader("set-cookie").getValue());
        } finally {
            response2.close();
        }
    }

    public static String getJson(String cookie) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            InputStream is = response1.getEntity().getContent();
            String gj = IOUtils.toString(is, "utf-8");
            return gj;
        } finally {
            response1.close();
        }
    }


}
