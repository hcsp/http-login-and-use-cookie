package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
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
        //将参数放入Map
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);

        String cookie = sendHttpPost("http://47.91.156.35:8000/auth/login", json);
        String result = sendHttpGet("http://47.91.156.35:8000/auth", cookie);

        System.out.println(cookie);
        System.out.println(result);
        return result;
    }

    //发送一个POST请求,传入url和json,返回json
    public static String sendHttpPost(String url, String json) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");

        //创建post实体
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse response =  httpClient.execute(httpPost);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);

        //获取JESSIONID的值
        String cookie = response.getLastHeader("Set-cookie").getValue();
        response.close();
        return cookie.replaceAll(";.*"," ");
    }

    //发送一个GET请求，传入url和cookie,返回一个json
    public static String sendHttpGet(String url, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type","application/json");
        httpGet.addHeader("Cookie",cookie);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity,"UTF-8");
        return result;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}
