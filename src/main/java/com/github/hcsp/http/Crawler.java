package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36");
        // 设置报文和通讯格式
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonParam = JSONObject.toJSONString(map);
        StringEntity stringEntity = new StringEntity(jsonParam, "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        Header[] headers = response.getHeaders("Set-Cookie");
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36");
        String[] headers_str = headers[0].getValue().replaceAll(";","").split(" ");
        httpGet.addHeader("Cookie", headers_str[0]);
        // 获取响应实体
        HttpEntity entity = response.getEntity();
        // 打印响应状态
        System.out.println(response.getStatusLine().getStatusCode());
        return EntityUtils.toString(entity);
}

    public static void main(String[] args) throws IOException {
        Crawler.loginAndGetResponse("xdml", "xdml");
    }
}
