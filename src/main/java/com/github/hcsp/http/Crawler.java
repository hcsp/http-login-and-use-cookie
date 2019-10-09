package com.github.hcsp.http;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 发送第一个请求
        String jsessionid = login(httpclient, username, password);
        // 发送第二个请求
        return getData(httpclient, jsessionid);
    }

    // 第二次请求获取数据
    private static String getData(CloseableHttpClient httpclient, String jsessionid) throws IOException {
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        // 设置请求头
        httpGet.setHeader("Cookie", jsessionid);
        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        InputStream content = response2.getEntity().getContent();
        return new String(ByteStreams.toByteArray(content));
    }

    // 登陆获取cookie
    private static String login(CloseableHttpClient httpclient, String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        // 设置请求头
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        // 设置参数
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        // 将请求参数转换成json数据
        String jsonBody = ObjectToJson(map);
        HttpEntity body = new StringEntity(jsonBody);
        httpPost.setEntity(body);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String jsessionid = "";
        try {
            String setCookie = response.getFirstHeader("Set-Cookie").toString();
            jsessionid = "JSESSIONID=" + setCookie.substring(setCookie.indexOf("=") + 1, setCookie.indexOf(";"));
        } finally {
            response.close();
        }
        return jsessionid;
    }

    // 将一个对象转换json格式的字符串
    private static String ObjectToJson(Map<String, String> map) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(map);
    }


}
