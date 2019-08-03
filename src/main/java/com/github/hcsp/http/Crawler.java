package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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
        //需要返回的String
        String str;
        //发送一个Post
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        httpPost.setHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json, "utf-8"));
        CloseableHttpResponse response = httpclient.execute(httpPost);


        try {
            //获取SESSIONID
            System.out.println(response.getStatusLine());
            Header[] header = response.getHeaders("Set-Cookie");
            StringBuilder SessionId = new StringBuilder();
            for (Header head : header) {
                SessionId.append(head);
            }
            String sessionId = SessionId.substring(12, 55);

            //新建get请求获取返回的String
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.setHeader("Cookie", sessionId);
            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            try {
                InputStream in = response1.getEntity().getContent();
                str = IOUtils.toString(in, "UTF-8");
            } finally {
                response1.close();
            }
        } finally {
            response.close();
        }
        return str;
    }
}
