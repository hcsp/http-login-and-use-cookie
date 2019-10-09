package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
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
    private static final String URI = "http://47.91.156.35:8000";
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String loginAndGetCookie(String username, String password, String target) throws IOException {
        HttpPost httpPost = new HttpPost(target);

        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        httpPost.setHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        return response.getHeaders("Set-Cookie")[0].getValue().split(";")[0].split("=")[1];
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = Crawler.loginAndGetCookie(username, password, URI + "/auth/login");
        System.out.println("cookie = " + cookie);
        HttpGet httpGet = new HttpGet(URI + "/auth");

        httpGet.addHeader("Set-Cookie", cookie);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

}
