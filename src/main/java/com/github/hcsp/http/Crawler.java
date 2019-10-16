package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //生成json字符串
        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        String json = JSON.toJSONString(user);
        //新建HTTP客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //设置请求参数
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
        httpPost.setEntity(entity);
        //发送请求,处理响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(response.getStatusLine());
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity);
    }

}
