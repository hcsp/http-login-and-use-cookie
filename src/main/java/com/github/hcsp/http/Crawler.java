package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", username);
        loginMap.put("password", password);
        String text = JSON.toJSONString(loginMap); //序列化
        httpPost.setEntity(new StringEntity(text));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        Header[] cookie = response2.getHeaders("Set-Cookie");
        String cookieValue = cookie[0].getValue(); // cookie

        // 登录
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth"); // 请求的接口
        httpGet.addHeader("Cookie", cookieValue);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        HttpEntity entity1 = response1.getEntity();
        StringWriter writer = new StringWriter();
        IOUtils.copy(entity1.getContent(), writer, StandardCharsets.UTF_8.name());
        String responseJson = writer.toString();
        System.out.println(responseJson);

        return responseJson;
    }

}
