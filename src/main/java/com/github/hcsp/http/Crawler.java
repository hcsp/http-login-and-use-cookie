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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        String cookie = sendHttpPost("http://47.91.156.35:8000/auth/login", json);
        return sendHttpGet("http://47.91.156.35:8000/auth", cookie);
    }

    public static String sendHttpPost(String url, String regJson) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36");

        httpPost.setEntity(new StringEntity(regJson, "UTF-8"));

        CloseableHttpResponse response = httpClient.execute(httpPost);

        String cookie = response.getFirstHeader("Set-Cookie").getValue();
        String reg = ";.*";
        String newStr = "";
        String str = cookie.replaceAll(reg, newStr);

        response.close();
        httpClient.close();
        return str;
    }

    public static String sendHttpGet(String url, String cookie) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse response = httpclient.execute(httpGet);

        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            String string = IOUtils.toString(inputStream, "UTF-8");
            return string;
        } finally {
            response.close();
        }
    }
}
