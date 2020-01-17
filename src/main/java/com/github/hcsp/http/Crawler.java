package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = null;
        //设置登录名和密码
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //设置请求头信息
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(map)));

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            //获取Set-Cookie
            Header[] headers = response.getHeaders("Set-Cookie");
            StringBuilder cookie = new StringBuilder();
            for (Header h : headers) {
                cookie.append(h.getValue());
            }
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            //设置cookie信息
            httpGet.setHeader("Cookie", cookie.toString().substring(0, cookie.indexOf(";")));
            try (CloseableHttpResponse response1 = httpclient.execute(httpGet);) {
                HttpEntity entity2 = response1.getEntity();
                body = IOUtils.toString(entity2.getContent(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return body;
    }

    public static void main(String[] args) throws Exception {
        Crawler.loginAndGetResponse("xdml", "xdml");
    }
}
