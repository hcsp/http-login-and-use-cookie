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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //创建请求客户端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建post一个请求体
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //添加header
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        System.out.println(httpPost);
        //添加body
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
        httpPost.setEntity(stringEntity);

        //通过客户端发送post请求
        CloseableHttpResponse response = httpclient.execute(httpPost);
        //拿到cookie
        String cookie;
        try {
            Header header = response.getHeaders("Set-Cookie")[0];
            cookie = header.getValue().replace("Set-Cookie: ", "").replace("; Path=/; HttpOnly", "");
        } finally {
            response.close();
        }
        //创建请求客户端
        CloseableHttpClient httpclient1 = HttpClients.createDefault();
        //创建get一个请求体
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        //添加cookie
        httpGet.addHeader("Cookie", cookie);
        //通过客户端发送get请求
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        //拿到返回内容转换成字符串
        String result;
        try {
            HttpEntity entity = response1.getEntity();
            InputStream content = entity.getContent();
            result = IOUtils.toString(content, StandardCharsets.UTF_8);
        } finally {
            response1.close();
        }
        return result;
    }
}