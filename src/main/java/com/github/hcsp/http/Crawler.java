package com.github.hcsp.http;


import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
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
    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36\\n\"");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String text = JSON.toJSONString(map);
        StringEntity entity = new StringEntity(text);
        httpPost.setEntity(entity);

        CloseableHttpResponse responsePost = httpclient.execute(httpPost);
        System.out.println(responsePost.getStatusLine());

        String cookie = responsePost.getFirstHeader("Set-Cookie").getValue();
        System.out.println(cookie);
        responsePost.close();

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        System.out.println(responseGet.getStatusLine());

        HttpEntity httpEntity = responseGet.getEntity();
        InputStream is = httpEntity.getContent();
        String html = (IOUtils.toString(is, "UTF-8"));
        responseGet.close();
        System.out.println(html);
        return html;
    }
}
