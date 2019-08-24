package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        // 通过 builder 模式创建一个具有 cookie 存储功能的客户端实例
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).build();
        // 尝试登录并被 set cookie
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(map)));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        httpclient.execute(httpPost).close();
        // 携带 cookie 进一步获取 auth 信息
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        CloseableHttpResponse authResponse = httpclient.execute(httpGet);
        try {
            InputStream is = authResponse.getEntity().getContent();
            String authResponseBody = IOUtils.toString(is, "UTF-8");
            System.out.println(authResponseBody);
            return authResponseBody;
        } finally {
            authResponse.close();
        }
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

}
