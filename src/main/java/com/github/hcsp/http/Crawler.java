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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        Map<String, String> resultMap = sendPost(new StringEntity(JSON.toJSONString(map)));

        // 发送 HTTP GET 请求，获取数据
        String finalResult = null;
        if (resultMap.get("cookie") != null) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.setHeader("cookie", resultMap.get("cookie"));
            CloseableHttpResponse getRsp = httpclient.execute(httpGet);
            HttpEntity getRspEntity = getRsp.getEntity();
            finalResult = IOUtils.toString(getRspEntity.getContent(), StandardCharsets.UTF_8);

            EntityUtils.consume(getRspEntity);
            getRsp.close();
        }

        return finalResult;
    }

    private static Map<String, String> sendPost(HttpEntity entity) throws IOException {
        Map<String, String> map = new HashMap<>();
        // 1. 发送 HTTP POST 请求，设置 Request Header 和 Body
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);

        // 2. 获取 “Set-Cookie” 响应头
        Header[] rspHeaders = response.getHeaders("set-cookie");
        if (rspHeaders.length != 0) {
            String theCookie = rspHeaders[0].getValue().substring(0, rspHeaders[0].getValue().indexOf("; Path=/; HttpOnly"));
            map.put("cookie", theCookie);
        }

        // 3. 获取 Response Body
        HttpEntity rspEntity = response.getEntity();
        String bodyStr = IOUtils.toString(rspEntity.getContent(), StandardCharsets.UTF_8);
        map.put("result", bodyStr);

        EntityUtils.consume(rspEntity);
        response.close();

        return map;
    }
}
