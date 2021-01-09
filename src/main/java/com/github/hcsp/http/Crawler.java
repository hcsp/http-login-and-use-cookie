package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //POST请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");

        //将map转为json
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));

        //获取Cookie
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Header[] headers = response.getHeaders("Set-Cookie");
        String setCookie = headers[0].toString();
        String cookie = setCookie.substring(setCookie.indexOf("JSESSIONID"), setCookie.indexOf(";"));
        response.close();

        //用Cookie发送请求
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse response1 = httpClient.execute(httpGet);
        HttpEntity entity1 = response1.getEntity();

        return EntityUtils.toString(entity1);
    }

    public static void main(String[] args) throws IOException {
        String str = loginAndGetResponse("xdml", "xdml");
    }
}
