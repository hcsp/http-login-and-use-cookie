package com.github.hcsp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //获得一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //生成一个http请求 http://47.91.156.35:8000/auth/login
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //header设置为JSON
        httpPost.addHeader("Content-Type", "application/json");
        //伪装成浏览器
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.113 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        //将map序列化成JSON串
        String json = new ObjectMapper().writeValueAsString(map);
        HttpEntity httpEntity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        httpPost.setEntity(httpEntity);

        //执行登录请求
        HttpResponse httpResponse = httpClient.execute(httpPost);
        //捞出cookie
        String cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();

        //携带cookie向http://47.91.156.35:8000/auth 发送get请求 并拿到响应体
        // 用EntityUtils将其序列化成字符串
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", cookie);
        httpEntity = httpClient.execute(httpPost).getEntity();
        String result = EntityUtils.toString(httpEntity);

        httpClient.close();
        return result;
    }
}
