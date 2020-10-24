package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36");
        CookieStore httpCookieStore = new BasicCookieStore();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String param = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(param));
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            // 执行请求
            Header[] heads = response.getHeaders("Set-Cookie");
            Header cookie = heads[0];
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.setHeader(cookie);
            CloseableHttpResponse res = httpclient.execute(httpGet);
            String content = EntityUtils.toString(res.getEntity(), "UTF-8");
            return content;
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }

    public static void main(String[] args) throws Exception {
        loginAndGetResponse("xdml", "xdml");
    }
}
