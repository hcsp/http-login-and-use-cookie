package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //建立一个客户端
        HttpClient httpclient = HttpClients.createDefault();
        //建立一个post请求，并添加url
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //添加报头
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Mobile Safari/537.36");
        //在body里面添加账号密码，要用JSON格式
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        StringEntity params = new StringEntity(json);
        httpPost.setEntity(params);
        //获得response响应
        HttpResponse response = httpclient.execute(httpPost);
        //提取cookie
        Header header = response.getFirstHeader("set-cookie");
        String cookie = header.getValue().split(";")[0];
        //使用该Cookie发送请求
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("cookie", cookie);
        HttpResponse responseGet = httpclient.execute(httpGet);
        HttpEntity entity = responseGet.getEntity();
        String response2 = IOUtils.toString(entity.getContent(), "UTF-8");

        return response2;
    }

//    public static void main(String[] args) throws IOException {
//        new Crawler().loginAndGetResponse("xdml","xdml");
//    }
}
